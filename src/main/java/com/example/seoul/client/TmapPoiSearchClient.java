package com.example.seoul.client;

import com.example.seoul.place.dto.PoiSearchResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class TmapPoiSearchClient {
    @Value("${tmap.api.key}")
    private String appKey;

    private static final String TMAP_POI_URL = "https://apis.openapi.sk.com/tmap/pois";
    public PoiSearchResponseDto searchPoi(String keyword, int count) {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(TMAP_POI_URL)
                .queryParam("version", 1)
                .queryParam("searchKeyword", keyword)
                .queryParam("radius", 10000)
                .queryParam("count", count)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("appKey", appKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<PoiSearchResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                PoiSearchResponseDto.class
        );
        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("TMAP POI 검색 실패: {}", response.getStatusCode());
            throw new IllegalArgumentException("POI SEARCH FAIL");
        }

        return response.getBody();
    }
}
