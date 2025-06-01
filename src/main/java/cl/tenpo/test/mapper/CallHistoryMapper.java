package cl.tenpo.test.mapper;

import cl.tenpo.test.dto.response.CallHistoryResponse;
import cl.tenpo.test.entity.CallHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

/**
 * Interface for mapping between {@code CallHistory} entities and {@code CallHistoryResponse} DTOs.
 * <p>
 * This interface uses MapStruct to define the mapping logic for transforming {@code CallHistory} entities
 * into their corresponding {@code CallHistoryResponse} representations.
 * </p>
 *
 * <p>
 * The {@code INSTANCE} field provides a singleton instance of the mapper, which can be used
 * to perform mapping operations.
 * </p>
 *
 * <p>
 * Additionally, this interface includes methods for mapping lists and pages of {@code CallHistory} entities
 * to lists and pages of {@code CallHistoryResponse} DTOs, enabling the conversion of multiple records
 * in a single operation.
 * </p>
 */
@Mapper
public interface CallHistoryMapper {

  /**
   * Singleton instance of the mapper for performing mapping operations.
   */
  CallHistoryMapper INSTANCE = Mappers.getMapper(CallHistoryMapper.class);

  /**
   * Maps a {@code CallHistory} entity to a {@code CallHistoryResponse} DTO.
   * <p>
   * This method uses MapStruct to convert a {@code CallHistory} entity into its corresponding
   * {@code CallHistoryResponse} representation.
   * </p>
   *
   * @param callHistory The {@code CallHistory} entity to be mapped.
   * @return The mapped {@code CallHistoryResponse} DTO.
   */
  CallHistoryResponse toCallHistoryResponse(CallHistory callHistory);

  /**
   * Maps a page of {@code CallHistory} entities to a page of {@code CallHistoryResponse} DTOs.
   * <p>
   * This method uses MapStruct to convert a page of {@code CallHistory} entities into their corresponding
   * page of {@code CallHistoryResponse} representations.
   * </p>
   *
   * @param callHistoryResponsePage The page of {@code CallHistory} entities to be mapped.
   * @return The mapped page of {@code CallHistoryResponse} DTOs.
   */
  default Page<CallHistoryResponse> toCallHistoryResponse(Page<CallHistory> callHistoryResponsePage) {
    return callHistoryResponsePage.map(this::toCallHistoryResponse);
  }
}