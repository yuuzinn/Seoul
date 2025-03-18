package com.example.seoul.client;

import com.example.seoul.pedestrian.dto.TmapPedestrianRouteRequest;
import com.example.seoul.pedestrian.dto.TmapPedestrianRouteResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class TmapPedestrianRouteClient {

    @Value("${tmap.api.key}")
    private String appKey;

    private static final String TMAP_PEDESTRIAN_ROUTE_URL = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1";

    public TmapPedestrianRouteResponse getPedestrianRoute(TmapPedestrianRouteRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        // 출발지, 목적지 이름은 URL 인코딩 처리
        String startNameEncoded = URLEncoder.encode(request.getStartName(), StandardCharsets.UTF_8);
        String endNameEncoded = URLEncoder.encode(request.getEndName(), StandardCharsets.UTF_8);

        // 수정된 요청 객체 생성
        TmapPedestrianRouteRequest encodedRequest = new TmapPedestrianRouteRequest(
                request.getStartX(),
                request.getStartY(),
                request.getEndX(),
                request.getEndY(),
                startNameEncoded,
                endNameEncoded,
                request.getPassList()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        headers.set("appKey", appKey);

        HttpEntity<TmapPedestrianRouteRequest> entity = new HttpEntity<>(encodedRequest, headers);

        ResponseEntity<TmapPedestrianRouteResponse> response = restTemplate.exchange(
                TMAP_PEDESTRIAN_ROUTE_URL,
                HttpMethod.POST,
                entity,
                TmapPedestrianRouteResponse.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("TMAP 보행자 경로 API 요청 실패: {}", response.getStatusCode());
            throw new IllegalArgumentException("보행자 경로 조회 실패");
        }

        return response.getBody();
    }
}
