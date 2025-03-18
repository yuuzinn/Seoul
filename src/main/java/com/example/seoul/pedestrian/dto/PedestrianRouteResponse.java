package com.example.seoul.pedestrian.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PedestrianRouteResponse {
    private int totalDistance;
    private int totalTime;
    private List<PointRoute> pointRoutes;
    private List<LineRoute> lineRoutes;

    @Getter
    @AllArgsConstructor
    public static class PointRoute {
        private double latitude;
        private double longitude;
        private String instruction; // 예: description
        // 필요 시 추가 필드 (name, direction 등)도 포함 가능
    }

    @Getter
    @AllArgsConstructor
    public static class LineRoute {
        private List<List<Double>> coordinates;
        private int distance;
        private int time;
        private String name;
        private String description;
        private int roadType;
        private int categoryRoadType;
    }
}
