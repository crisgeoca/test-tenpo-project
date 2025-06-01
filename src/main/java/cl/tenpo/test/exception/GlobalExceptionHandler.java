package cl.tenpo.test.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for managing application-wide exceptions.
 * <p>
 * This class provides centralized exception handling for the application using Spring's
 * {@code @RestControllerAdvice}. It captures specific and generic exceptions, logs the details,
 * and returns appropriate HTTP responses.
 * </p>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *   <li>Handles custom exceptions like {@code PercentageUnavailableException}.</li>
 *   <li>Handles generic exceptions to prevent unhandled errors.</li>
 *   <li>Logs error details for debugging purposes.</li>
 * </ul>
 *
 * <p><b>Thread Safety:</b></p>
 * <ul>
 *   <li>This class is thread-safe as it relies on Spring's dependency injection and stateless methods.</li>
 * </ul>
 *
 * @author Christian Giovani Cachaya Bolivar
 * @version 1.0
 */
@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles {@code PercentageUnavailableException} and returns a SERVICE_UNAVAILABLE response.
   * <p>
   * This method logs the exception details and sends an HTTP 503 response with the exception message.
   * </p>
   *
   * @param ex The {@code PercentageUnavailableException} thrown during application execution.
   * @return A {@code ResponseEntity} containing the exception message and HTTP 503 status.
   */
  @ExceptionHandler(PercentageUnavailableException.class)
  public ResponseEntity<String> handlePercentageException(PercentageUnavailableException ex) {
    log.error("PercentageUnavailableException occurred: {}", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
  }

  /**
   * Handles generic exceptions and returns an INTERNAL_SERVER_ERROR response.
   * <p>
   * This method logs the exception details and sends an HTTP 500 response with a generic error message.
   * </p>
   *
   * @param ex The {@code Exception} thrown during application execution.
   * @return A {@code ResponseEntity} containing a generic error message and HTTP 500 status.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGenericException(Exception ex) {
    log.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
  }
}