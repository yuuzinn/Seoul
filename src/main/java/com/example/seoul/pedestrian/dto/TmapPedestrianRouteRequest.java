package com.example.seoul.pedestrian.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TmapPedestrianRouteRequest {
    private double startX;    // 출발지 경도
    private double startY;    // 출발지 위도
    private double endX;      // 목적지 경도
    private double endY;      // 목적지 위도
    private String startName; // 출발지 이름 (UTF-8 인코딩 필요)
    private String endName;   // 목적지 이름 (UTF-8 인코딩 필요)
    private String passList;  // 3개 이상일 시 추가
}
