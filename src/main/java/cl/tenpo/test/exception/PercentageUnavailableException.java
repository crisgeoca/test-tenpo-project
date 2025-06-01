package cl.tenpo.test.exception;

/**
 * Exception thrown when the percentage service is unavailable.
 * <p>
 * This runtime exception is used to indicate that the percentage calculation
 * service cannot be accessed or is temporarily unavailable. It provides a
 * message detailing the reason for the exception.
 * </p>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *   <li>Extends {@code RuntimeException} for unchecked exception handling.</li>
 *   <li>Includes a constructor to specify the exception message.</li>
 * </ul>
 *
 * <p><b>Usage:</b></p>
 * <ul>
 *   <li>Thrown by service methods when the percentage service is unavailable.</li>
 *   <li>Handled by the {@code GlobalExceptionHandler} to return appropriate HTTP responses.</li>
 * </ul>
 *
 * @author Christian Giovani Cachaya Bolivar
 * @version 1.0
 */
public class PercentageUnavailableException extends RuntimeException {
  public PercentageUnavailableException(String message) {
    super(message);
  }
}
