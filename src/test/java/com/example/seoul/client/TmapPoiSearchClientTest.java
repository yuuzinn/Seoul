package com.example.seoul.client;

import com.example.seoul.exception.CustomException;
import com.example.seoul.exception.ErrorCode;
import com.example.seoul.place.dto.PoiSearchResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TmapPoiSearchClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TmapPoiSearchClient tmapPoiSearchClient;

    private String appKey = "test-app-key"; // 테스트용 appKey

    @BeforeEach
    void setUp() {
        try {
            var field = TmapPoiSearchClient.class.getDeclaredField("appKey");
            field.setAccessible(true);
            field.set(tmapPoiSearchClient, appKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject appKey for testing");
        }
    }

    @Test
    @DisplayName("POI 검색 정상적으로 성공")
    void testSearchPoi_Success() {
        PoiSearchResponseDto mockResponse = new PoiSearchResponseDto();
        ResponseEntity<PoiSearchResponseDto> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(any(String.class), any(), any(), eq(PoiSearchResponseDto.class)))
                .thenReturn(responseEntity);

        PoiSearchResponseDto result = tmapPoiSearchClient.searchPoi("홍대", 5);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("POI 검색 실패 시 EXCEPTION 발생")
    void testSearchPoi_Fail_ThrowsException() {
        ResponseEntity<PoiSearchResponseDto> errorResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.exchange(any(String.class), any(), any(), eq(PoiSearchResponseDto.class)))
                .thenReturn(errorResponse);

        assertThatThrownBy(() -> tmapPoiSearchClient.searchPoi("홍대", 5))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.POT_SEARCH_FAIL.getMessage());
    }
}
