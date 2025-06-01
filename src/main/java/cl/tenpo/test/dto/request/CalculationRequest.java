package cl.tenpo.test.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for representing the input data required for a calculation request.
 * <p>
 * This class encapsulates two numerical values that will be used in a calculation
 * involving a percentage adjustment. It is designed to be used as part of the API
 * request payload.
 * </p>
 *
 * <p><b>Fields:</b></p>
 * <ul>
 *   <li>{@code num1}: The first number for the calculation.</li>
 *   <li>{@code num2}: The second number for the calculation.</li>
 * </ul>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *   <li>Uses Swagger annotations to provide API documentation for the fields.</li>
 *   <li>Includes Lombok annotations for automatic generation of getters, setters, and other utility methods.</li>
 * </ul>
 *
 * @author Christian Giovani Cachaya Bolivar
 * @version 1.0
 */
@Data
@Builder
public class CalculationRequest {

  @Schema(
      description = "First number for calculation",
      example = "10.5"
  )
  @Min(value = 0, message = "num1 must be greater than or equal to 0")
  @NotNull(message = "num1 cannot be null")
  private Double num1;

  @Schema(
      description = "Second number for calculation",
      example = "5.5"
  )
  @NotNull(message = "num2 cannot be null")
  @Min(value = 0, message = "num2 must be greater than or equal to 0")
  private Double num2;
}
