package cl.tenpo.test.service.impl;

import cl.tenpo.test.dto.request.CalculationRequest;
import cl.tenpo.test.dto.response.CalculationResponse;
import cl.tenpo.test.dto.response.CallHistoryResponse;
import cl.tenpo.test.entity.CallHistory;
import cl.tenpo.test.repository.CallHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import redis.clients.jedis.UnifiedJedis;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PercentageServiceImplTest {

  @InjectMocks
  private PercentageServiceImpl percentageService;

  @Mock
  private CallHistoryRepository callHistoryRepository;

  @Mock
  private UnifiedJedis jedis;

  @Captor
  private ArgumentCaptor<CallHistory> callHistoryCaptor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCalculateWithPercentage_Success() {
    CalculationRequest request = CalculationRequest.builder()
        .num1(10.0)
        .num2(20.0)
        .build();
    when(jedis.get("external_percentage")).thenReturn("10.0");

    CalculationResponse response = percentageService.calculateWithPercentage(request);

    assertNotNull(response);
    assertEquals(30, response.getSum());
    assertEquals(33, response.getResultWithPercentage());
    assertEquals(10.0, response.getAppliedPercentage());
    verify(jedis, times(1)).get("external_percentage");
    verify(callHistoryRepository, times(1)).save(callHistoryCaptor.capture());
    CallHistory savedHistory = callHistoryCaptor.getValue();
    assertEquals("/api/calculate", savedHistory.getEndpoint());
  }

  @Test
  void testGetCallHistory_Success() {
    CallHistory callHistory = CallHistory.builder()
        .date(LocalDateTime.now())
        .endpoint("/api/test")
        .parameters("param")
        .responseOrError("response")
        .build();
    when(callHistoryRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(callHistory)));

    Page<CallHistoryResponse> history = percentageService.getCallHistory(Pageable.unpaged());

    assertNotNull(history);
    assertEquals(1, history.getContent().size());
    verify(callHistoryRepository, times(1)).findAll(any(Pageable.class));
  }

  @Test
  void testLogCallAsync() {
    String endpoint = "/api/test";
    String params = "param";
    String responseOrError = "response";

    percentageService.logCallAsync(endpoint, params, responseOrError);

    verify(callHistoryRepository, times(1)).save(callHistoryCaptor.capture());
    CallHistory savedHistory = callHistoryCaptor.getValue();
    assertEquals(endpoint, savedHistory.getEndpoint());
    assertEquals(params, savedHistory.getParameters());
    assertEquals(responseOrError, savedHistory.getResponseOrError());
  }
}