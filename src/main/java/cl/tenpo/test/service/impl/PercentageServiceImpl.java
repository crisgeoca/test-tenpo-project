package cl.tenpo.test.service.impl;

import cl.tenpo.test.dto.request.CalculationRequest;
import cl.tenpo.test.dto.response.CalculationResponse;
import cl.tenpo.test.dto.response.CallHistoryResponse;
import cl.tenpo.test.entity.CallHistory;
import cl.tenpo.test.exception.PercentageUnavailableException;
import cl.tenpo.test.mapper.CallHistoryMapper;
import cl.tenpo.test.repository.CallHistoryRepository;
import cl.tenpo.test.service.interfaces.PercentageService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import redis.clients.jedis.UnifiedJedis;

import java.time.LocalDateTime;

/**
 * Implementation of the {@code PercentageService} interface for handling percentage-based calculations and call history management.
 * <p>
 * This class provides methods to perform calculations using a percentage value, manage caching of the percentage
 * in Redis, retrieve call history, and log API calls asynchronously. It integrates with external services
 * and a database repository to ensure efficient and reliable operations.
 * </p>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *   <li>Calculates the sum of two numbers and applies a percentage adjustment.</li>
 *   <li>Retrieves and caches percentage values from Redis or external services.</li>
 *   <li>Handles call history retrieval and asynchronous logging of API calls.</li>
 *   <li>Logs detailed information about operations and errors.</li>
 * </ul>
 *
 * <p><b>Dependencies:</b></p>
 * <ul>
 *   <li>{@code CallHistoryRepository}: For database operations related to call history.</li>
 *   <li>{@code UnifiedJedis}: For Redis operations.</li>
 *   <li>{@code CallHistoryMapper}: For mapping entities to DTOs.</li>
 * </ul>
 *
 * <p><b>Exception Handling:</b></p>
 * <ul>
 *   <li>Throws {@code PercentageUnavailableException} for issues with percentage retrieval or caching.</li>
 *   <li>Logs and rethrows unexpected exceptions.</li>
 * </ul>
 *
 * <p><b>Thread Safety:</b></p>
 * <ul>
 *   <li>Uses {@code @Async} for asynchronous operations to improve performance.</li>
 *   <li>Ensures thread safety by using local variables and read-only operations.</li>
 * </ul>
 *
 * <p><b>Logging:</b></p>
 * <ul>
 *   <li>Logs the start and completion of operations.</li>
 *   <li>Logs errors and exceptions encountered during execution.</li>
 * </ul>
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class PercentageServiceImpl implements PercentageService {

  private static final String CACHE_KEY = "external_percentage";

  public static final CallHistoryMapper MAPPER = CallHistoryMapper.INSTANCE;

  private final CallHistoryRepository callHistoryRepository;
  private final UnifiedJedis jedis;

  /**
   * Performs a calculation using the provided numbers and a percentage value.
   * <p>
   * This method calculates the sum of the two numbers from the {@code CalculationRequest} and applies
   * a percentage adjustment to the result. It first attempts to retrieve the percentage value from the cache.
   * If the cached value is unavailable, it calls an external service to fetch the percentage and caches it for future use.
   * </p>
   * <p>
   * If the external service fails and no cached value is available, a {@code PercentageUnavailableException} is thrown.
   * The method logs the calculation details and stores the call history asynchronously, including both successful and failed operations.
   * </p>
   * <p>
   * In case of unexpected errors, a {@code RuntimeException} is thrown with details about the error.
   * </p>
   *
   * <p><b>Logging:</b></p>
   * <ul>
   *   <li>Logs the start of the calculation with the input numbers.</li>
   *   <li>Logs the retrieval of the percentage value from the cache or external service.</li>
   *   <li>Logs the caching process of the percentage value.</li>
   *   <li>Logs the successful completion of the calculation or any errors encountered.</li>
   * </ul>
   *
   * <p><b>Thread Safety:</b></p>
   * <ul>
   *   <li>This method is thread-safe as it uses local variables and performs asynchronous logging.</li>
   * </ul>
   *
   * @param request The {@code CalculationRequest} containing the two numbers for the calculation.
   * @return A {@code CalculationResponse} containing the sum, adjusted result, and percentage used.
   * @throws PercentageUnavailableException If the percentage value cannot be retrieved from the cache or external service.
   * @throws RuntimeException If an unexpected error occurs during the calculation.
   */
  @Override
  public CalculationResponse calculateWithPercentage(CalculationRequest request) {
    log.info("Starting calculation with num1: {} and num2: {}", request.getNum1(), request.getNum2());
    double sum = request.getNum1() + request.getNum2();

    try {
      Double percentage = Optional.ofNullable(getCachedPercentage())
          .orElseGet(() -> {
            log.info("No cached percentage found. Calling external service...");
            Double externalPercentage = callExternalPercentageService();
            if (externalPercentage == null) {
              log.error("External percentage service failed and no cache available.");
              throw new PercentageUnavailableException("Percentage service failed and no cache available.");
            }
            cachePercentage(externalPercentage);
            log.info("Percentage cached successfully: {}", externalPercentage);
            return externalPercentage;
          });

      double result = sum + (sum * (percentage / 100));
      CalculationResponse response = CalculationResponse.builder()
          .sum(sum)
          .resultWithPercentage(result)
          .appliedPercentage(percentage)
          .build();
      log.info("Calculation completed successfully. Response: {}", response);
      logCallAsync("/api/calculate", request.toString(), response.toString());
      return response;
    } catch (PercentageUnavailableException e) {
      log.error("Percentage unavailable: {}", e.getMessage(), e);
      logCallAsync("/api/calculate", request.toString(), e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error during calculation: {}", e.getMessage(), e);
      logCallAsync("/api/calculate", request.toString(), e.getMessage());
      throw e;
    }
  }

  /**
   * Retrieves the call history as a list of {@code CallHistoryResponse} DTOs.
   * <p>
   * This method fetches all call history records from the database using the {@code CallHistoryRepository}.
   * The records are mapped to their corresponding {@code CallHistoryResponse} representations
   * using the {@code CallHistoryMapper}.
   * </p>
   *
   * <p><b>Logging:</b></p>
   * <ul>
   *   <li>Logs the start of the call history retrieval process.</li>
   *   <li>Logs the total number of records retrieved.</li>
   * </ul>
   *
   * <p><b>Thread Safety:</b></p>
   * <ul>
   *   <li>This method is thread-safe as it performs read-only operations on the database.</li>
   * </ul>
   *
   * @return A list of {@code CallHistoryResponse} DTOs representing the call history.
   * @throws RuntimeException If an unexpected error occurs during the retrieval process.
   */
  public Page<CallHistoryResponse> getCallHistory(Pageable pageable) {
    log.info("Fetching call history with pagination...");
    Page<CallHistory> historyPage = callHistoryRepository.findAll(pageable);
    Page<CallHistoryResponse> responsePage = historyPage.map(MAPPER::toCallHistoryResponse);
    log.info("Call history fetched successfully. Total records: {}", responsePage.getTotalElements());
    return responsePage;
  }

  /**
   * Calls an external service to retrieve the percentage value.
   * <p>
   * This method simulates a call to an external service to fetch the percentage value.
   * If the call is successful, it returns a fixed value of {@code 10.0}.
   * In case of an exception, it logs the error and returns {@code null}.
   * </p>
   *
   * @return The percentage value as a {@code Double}, or {@code null} if the external service fails.
   */
  private Double callExternalPercentageService() {
    try {
      log.info("Calling external percentage service...");
      return 10.0;
    } catch (Exception e) {
      log.error("Error while calling external percentage service: {}", e.getMessage(), e);
      return null;
    }
  }

  /**
   * Caches the percentage value in Redis.
   * <p>
   * This method stores the given percentage value in Redis using the {@code CACHE_KEY}.
   * The value is set to expire after 1800 seconds (30 minutes).
   * If an error occurs during the caching process, it logs the error and throws
   * a {@code PercentageUnavailableException}.
   * </p>
   *
   * @param value The percentage value to be cached as a {@code Double}.
   * @throws PercentageUnavailableException If an error occurs while caching the percentage value in Redis.
   */
  private void cachePercentage(Double value) {
    log.info("Caching percentage value: {}", value);
    try {
      jedis.setex(CACHE_KEY, 1800, String.valueOf(value));
    } catch (Exception e) {
      log.error("Failed to cache percentage value: {}", e.getMessage(), e);
      throw new PercentageUnavailableException(String.format("Failed to cache percentage value: %s", e.getMessage()));
    }
  }

  /**
   * Retrieves the cached percentage value from Redis.
   * <p>
   * This method attempts to fetch the value associated with the {@code CACHE_KEY} from Redis.
   * If the value exists, it is parsed into a {@code Double} and returned.
   * If the value is not found or an error occurs during the operation,
   * an error is logged and a {@code PercentageUnavailableException} is thrown.
   * </p>
   *
   * @return The cached percentage value as a {@code Double}, or {@code null} if not found.
   * @throws PercentageUnavailableException If an error occurs while connecting to or retrieving the value from Redis.
   */
  private Double getCachedPercentage() {
    log.info("Checking cached percentage...");
    try {
      String value = jedis.get(CACHE_KEY);
      if (value != null) {
        log.info("Cached percentage found: {}", value);
        return Double.parseDouble(value);
      }
      log.info("No cached percentage found.");
      return null;
    } catch (Exception e) {
      log.error("Failed to retrieve cached percentage: {}", e.getMessage(), e);
      throw new PercentageUnavailableException(String.format("Failed to retrieve cached percentage: %s", e.getMessage()));
    }
  }

  /**
   * Logs an API call asynchronously by saving its details to the database.
   * <p>
   * This method creates a {@code CallHistory} entity using the provided endpoint, parameters,
   * and response or error details, and saves it to the database asynchronously.
   * </p>
   *
   * <p>
   * The method logs the process of saving the call history, including the endpoint being logged,
   * the parameters sent, and the success or failure of the operation.
   * </p>
   *
   * @param endpoint The endpoint of the API call.
   * @param params The parameters sent with the API call.
   * @param responseOrError The response or error returned from the API call.
   */
  @Async
  public void logCallAsync(String endpoint, String params, String responseOrError) {
    log.info("Logging call asynchronously for endpoint: {}", endpoint);
    callHistoryRepository.save(CallHistory.builder()
        .date(LocalDateTime.now())
        .endpoint(endpoint)
        .parameters(params)
        .responseOrError(responseOrError)
        .build());
    log.info("Call logged successfully.");
  }
}