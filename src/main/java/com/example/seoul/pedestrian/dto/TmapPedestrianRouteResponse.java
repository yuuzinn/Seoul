package com.example.seoul.pedestrian.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TmapPedestrianRouteResponse {

    private String type; // 예: FeatureCollection
    private List<Feature> features;

    @Getter
    @Setter
    public static class Feature {
        private String type;
        private Geometry geometry;
        private Properties properties;
    }

    @Getter
    @Setter
    public static class Geometry {
        private String type;

        @JsonProperty("coordinates")
        private Object coordinates; // Point: List<Double>, LineString: List<List<Double>>
    }

    @Getter
    @Setter
    public static class Properties {
        // 출발지(Point)일 때 공통 정보
        private int totalDistance; // 총 거리 (m)
        private int totalTime;     // 총 소요시간 (초)

        // 공통 속성
        private int index;
        private String name;
        private String description;

        // Point 타입 전용 속성
        private Integer pointIndex;
        private String direction;
        private String nearPoiName;
        private String nearPoiX;
        private String nearPoiY;
        private String intersectionName;
        private String facilityType;
        private String facilityName;
        private Integer turnType;
        private String pointType;

        // LineString 타입 전용 속성
        private Integer lineIndex;
        private Integer distance;
        private Integer time;
        private Integer roadType;
        private Integer categoryRoadType;
    }
}
