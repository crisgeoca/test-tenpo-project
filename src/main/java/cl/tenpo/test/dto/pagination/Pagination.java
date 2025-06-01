package cl.tenpo.test.dto.pagination;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.domain.Page;

/**
 * Represents pagination details for a paginated response.
 * <p>
 * This class encapsulates information about the current page, total items, total pages,
 * and the number of items per page in a paginated result.
 * </p>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *   <li>Provides a builder pattern for creating instances of {@code Pagination}.</li>
 *   <li>Includes a static factory method {@code of} to construct a {@code Pagination} object
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
public class Pagination {

  /**
   * The current page number (1-based index).
   */
  int currentPage;

  /**
   * The total number of items across all pages.
   */
  long totalItems;

  /**
   * The total number of pages available.
   */
  int totalPages;

  /**
   * The number of items on the current page.
   */
  int itemsPerPage;

  /**
   * Creates a {@code Pagination} object from a {@code Page} object.
   * <p>
   * This method extracts pagination details from the given {@code Page} object
   * and constructs a {@code Pagination} instance.
   * </p>
   *
   * @param response The {@code Page} object containing pagination details.
   * @return A {@code Pagination} object representing the pagination details.
   */
  public static Pagination of(Page<?> response) {
    return Pagination.builder()
        .itemsPerPage(response.getNumberOfElements())
        .currentPage(response.getNumber() + 1)
        .totalItems(response.getTotalElements())
        .totalPages(response.getTotalPages())
        .build();
  }
}