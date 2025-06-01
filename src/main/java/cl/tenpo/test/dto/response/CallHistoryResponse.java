package cl.tenpo.test.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for representing the call history of API requests.
 * <p>
 * This class encapsulates details about individual API calls, including the endpoint called,
 * the parameters sent, the response or error message, and the timestamp of the call.
 * It is designed to be used as part of the API response payload for call history retrieval.
 * </p>
 *
 * <p><b>Fields:</b></p>
 * <ul>
 *   <li>{@code id}: Unique identifier for the call history entry.</li>
 *   <li>{@code date}: Date and time when the call was made.</li>
 *   <li>{@code endpoint}: The endpoint that was called.</li>
 *   <li>{@code parameters}: The parameters sent in the call.</li>
 *   <li>{@code responseOrError}: The response or error message from the call.</li>
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
@AllArgsConstructor
@Builder
public class CallHistoryResponse {

  @Schema(
      description = "Unique identifier for the call history entry",
      example = "1"
  )
  private Long id;

  @Schema(
      description = "Date and time when the call was made",
      example = "2023-10-01T12:00:00"
  )
  private LocalDateTime date;

  @Schema(
      description = "Endpoint that was called",
      example = "/api/calculate"
  )
  private String endpoint;

  @Schema(
      description = "Parameters sent in the call",
      example = """
          {
            "num1": 10.5,
            "num2": 5.5
          }"""
  )
  private String parameters;

  @Schema(
      description = "Response or error message from the call",
      example = """
          {
            "sum": 16.0,
            "result": 17.6,
            "percentage": 10.0
          }"""
  )
  private String responseOrError;

}
