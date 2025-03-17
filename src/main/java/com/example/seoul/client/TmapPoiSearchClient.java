package com.example.seoul.client;

import com.example.seoul.place.dto.PoiSearchResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class TmapPoiSearchClient {

    @Value("${tmap.api.key}")
    private String appKey;

    private static final String TMAP_POI_URL = "https://apis.openapi.sk.com/tmap/pois";

    public PoiSearchResponseDto searchPoi(String keyword, int count) {
        RestTemplate restTemplate = new RestTemplate();

        int page = 1;
        String reqCoordType = "WGS84GEO";
        String multiPoint = "Y";
        String searchtypCd = "A";

        String keywordUTF8 = new String(keyword.getBytes(StandardCharsets.UTF_8));

        String url = UriComponentsBuilder.fromHttpUrl(TMAP_POI_URL)
                .queryParam("version", 1)
                .queryParam("page", page)
                .queryParam("count", count)
                .queryParam("searchKeyword", keywordUTF8)
                .queryParam("searchtypCd", searchtypCd)
                .queryParam("reqCoordType", reqCoordType)
                .queryParam("multiPoint", multiPoint)
                .queryParam("areaLLCode", "11")
                .queryParam("areaLMCode", "000")
                .queryParam("searchType", "name")
                .build()
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
