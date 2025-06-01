package cl.tenpo.test.dto.pagination;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.domain.Page;

/**
 * Represents a paginated response containing data and pagination details.
 * <p>
 * This class encapsulates the list of data items and the pagination metadata
 * for a paginated result.
 * </p>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *   <li>Provides a builder pattern for creating instances of {@code PaginatedResponse}.</li>
 *   <li>Includes a static factory method {@code of} to construct a {@code PaginatedResponse} object
 *       from a {@code Page} object.</li>
 * </ul>
 *
 * <p><b>Thread Safety:</b></p>
 * <ul>
 *   <li>This class is immutable and thread-safe.</li>
 * </ul>
 *
 * @author Christian Giovani Cachaya Bolivar
 * @version 1.0
 */
@Value
@Builder
public class PaginatedResponse {

  /**
   * The list of data items in the current page.
   */
  List<?> data;

  /**
   * The pagination metadata for the response.
   */
  Pagination pagination;

  /**
   * Creates a {@code PaginatedResponse} object from a {@code Page} object.
   * <p>
   * This method extracts the data and pagination details from the given {@code Page} object
   * and constructs a {@code PaginatedResponse} instance.
   * </p>
   *
   * @param response The {@code Page} object containing data and pagination details.
   * @return A {@code PaginatedResponse} object representing the paginated response.
   */
  public static PaginatedResponse of(Page<?> response) {
    return PaginatedResponse.builder()
        .data(response.getContent())
        .pagination(Pagination.of(response))
        .build();
  }
}