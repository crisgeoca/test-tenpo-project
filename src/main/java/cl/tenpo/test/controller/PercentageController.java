package cl.tenpo.test.controller;

import cl.tenpo.test.dto.pagination.PaginatedResponse;
import cl.tenpo.test.dto.request.CalculationRequest;
import cl.tenpo.test.dto.response.CalculationResponse;
import cl.tenpo.test.dto.response.CallHistoryResponse;
import cl.tenpo.test.service.impl.PercentageServiceImpl;
import cl.tenpo.test.service.interfaces.PercentageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling percentage-based calculations and call history retrieval.
 * <p>
 * This controller provides endpoints for performing calculations with a percentage adjustment
 * and retrieving the call history of API requests. It delegates the business logic to the
 * {@code PercentageServiceImpl} and returns the results wrapped in {@code ResponseEntity}.
 * </p>
 *
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code POST /api/calculate}: Performs a calculation using two numbers and a percentage.</li>
 *   <li>{@code GET /api/history}: Retrieves the call history of API requests.</li>
 * </ul>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *   <li>Logs incoming requests and their details.</li>
 *   <li>Handles API responses with appropriate HTTP status codes.</li>
 * </ul>
 *
 * <p><b>Thread Safety:</b></p>
 * <ul>
 *   <li>This class is thread-safe as it relies on Spring's dependency injection and stateless service methods.</li>
 * </ul>
 *
 * @author Christian Giovani Cachaya Bolivar
 * @version 1.0
 */
@Log4j2
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PercentageController {

  private final PercentageService percentageService;

  /**
   * Handles the calculation request by performing a sum and applying a percentage adjustment.
   * <p>
   * This method receives a {@code CalculationRequest} containing two numbers, delegates the calculation
   * to the {@code PercentageServiceImpl}, and returns the result wrapped in a {@code ResponseEntity}.
   * </p>
   *
   * <p><b>Logging:</b></p>
   * <ul>
   *   <li>Logs the received request with the input numbers.</li>
   *   <li>Logs the result of the calculation.</li>
   * </ul>
   *
   * <p><b>API Responses:</b></p>
   * <ul>
   *   <li>Returns HTTP 200 with the calculation result if successful.</li>
   *   <li>Returns HTTP 500 if an internal server error occurs.</li>
   *   <li>Returns HTTP 503 if the service is unavailable.</li>
   * </ul>
   *
   * <p><b>Thread Safety:</b></p>
   * <ul>
   *   <li>This method is thread-safe as it relies on Spring's dependency injection and stateless service methods.</li>
   * </ul>
   *
   * @param request The {@code CalculationRequest} containing the numbers for the calculation.
   * @return A {@code ResponseEntity} containing the {@code CalculationResponse} with the calculation result.
   */
  @PostMapping("/calculate")
  @Operation(summary = "Calculate sum and percentage result")
  @ApiResponse(responseCode = "200", description = "Calculation successful")
  @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(responseCode = "503", description = "Service unavailable", content = @Content(schema = @Schema(hidden = true)))
  public ResponseEntity<CalculationResponse> calculate(@Valid @RequestBody CalculationRequest request) {
    log.info("Received request to calculate with num1: {} and num2: {}", request.getNum1(), request.getNum2());
    return ResponseEntity.ok(percentageService.calculateWithPercentage(request));
  }

  /**
   * Retrieves the paginated call history of API requests.
   * <p>
   * This method handles a GET request to fetch the call history stored in the database
   * with pagination support. It validates the {@code page} and {@code size} parameters
   * to ensure they meet the minimum requirements and delegates the retrieval process
   * to the {@code PercentageServiceImpl}. The results are returned wrapped in a
   * {@code ResponseEntity}.
   * </p>
   *
   * <p><b>Validation:</b></p>
   * <ul>
   *   <li>The {@code page} parameter must be at least 1. If not provided, it defaults to 1.</li>
   *   <li>The {@code size} parameter must be at least 1. If not provided, it defaults to 10.</li>
   * </ul>
   *
   * <p><b>Pagination:</b></p>
   * <ul>
   *   <li>Accepts {@code page} and {@code size} parameters to control pagination.</li>
   *   <li>Sorts the results by the {@code id} field in ascending order.</li>
   * </ul>
   *
   * <p><b>Logging:</b></p>
   * <ul>
   *   <li>Logs the receipt of the request to fetch call history.</li>
   *   <li>Logs the page number and size used for pagination.</li>
   * </ul>
   *
   * <p><b>API Responses:</b></p>
   * <ul>
   *   <li>Returns HTTP 200 with the paginated call history if successful.</li>
   *   <li>Returns HTTP 500 if an internal server error occurs.</li>
   *   <li>Returns HTTP 503 if the service is unavailable.</li>
   * </ul>
   *
   * <p><b>Thread Safety:</b></p>
   * <ul>
   *   <li>This method is thread-safe as it relies on Spring's dependency injection and stateless service methods.</li>
   * </ul>
   *
   * @param page The page number to retrieve (1-based index). Must be at least 1.
   * @param size The number of items per page. Must be at least 1.
   * @return A {@code ResponseEntity} containing a {@code PaginatedResponse} with the paginated call history.
   */
  @GetMapping("/history")
  @Operation(summary = "Get call history")
  @ApiResponse(responseCode = "200", description = "Call history fetched successfully")
  @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(hidden = true)))
  @ApiResponse(responseCode = "503", description = "Service unavailable", content = @Content(schema = @Schema(hidden = true)))
  public ResponseEntity<PaginatedResponse> history(
      @Schema(example = "1") @RequestParam(defaultValue = "1") @Valid @NotNull @Min(value = 1, message = "The page number must be at least 1") Integer page,
      @Schema(example = "10") @RequestParam(defaultValue = "10") @Valid @NotNull @Min(value = 1, message = "The items per page must be at least 1") Integer size) {
    log.info("Received request to fetch call history");
    Pageable paging = PageRequest.of(page-1, size, Sort.by(Order.asc("id")));
    Page<CallHistoryResponse> callHistory = percentageService.getCallHistory(paging);
    PaginatedResponse paginatedResponse = PaginatedResponse.of(callHistory);
    return ResponseEntity.ok(paginatedResponse);
  }
}