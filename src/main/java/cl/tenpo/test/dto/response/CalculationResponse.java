package cl.tenpo.test.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for representing the response of a calculation request.
 * <p>
 * This class encapsulates the results of a calculation, including the sum of the input numbers,
 * the result after applying a percentage adjustment, and the percentage applied.
 * It is designed to be used as part of the API response payload.
 * </p>
 *
 * <p><b>Fields:</b></p>
 * <ul>
 *   <li>{@code sum}: The sum of the numbers provided in the request.</li>
 *   <li>{@code resultWithPercentage}: The result of applying the percentage to the sum.</li>
 *   <li>{@code appliedPercentage}: The percentage applied to the sum.</li>
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
@AllArgsConstructor
public class CalculationResponse {

  @Schema(
      description = "The sum of the numbers provided in the request",
      example = "15.0"
  )
  private Double sum;

  @Schema(
      description = "The result of applying the percentage to the sum",
      example = "3.0"
  )
  private double resultWithPercentage;

  @Schema(
      description = "The percentage applied to the sum",
      example = "20.0"
  )
  private Double appliedPercentage;
}
