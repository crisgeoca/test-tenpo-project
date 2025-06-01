package cl.tenpo.test.service.interfaces;

import cl.tenpo.test.dto.request.CalculationRequest;
import cl.tenpo.test.dto.response.CalculationResponse;
import cl.tenpo.test.dto.response.CallHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PercentageService {

  CalculationResponse calculateWithPercentage(CalculationRequest request);

  Page<CallHistoryResponse> getCallHistory(Pageable pageable);

}
