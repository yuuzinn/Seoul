package com.example.seoul.place.dto;

import lombok.Getter;
import lombok.ToString;
import java.util.List;

@Getter
@ToString
public class PoiSearchResponseDto {

    private SearchPoiInfo searchPoiInfo;

    @Getter
    @ToString
    public static class SearchPoiInfo {
        private int totalCount;
        private int count;
        private int page;
        private Pois pois; // pois 객체를 포함
    }

    @Getter
    @ToString
    public static class Pois {
        private List<Poi> poi; // poi 리스트
    }

    @Getter
    @ToString
    public static class Poi {
        private String name;        // 장소명
        private String frontLat;    // 위도
        private String frontLon;    // 경도
        private String upperAddrName;   // 주소(시/도)
        private String middleAddrName;  // 주소(구/군)
        private String lowerAddrName;   // 주소(동/읍/면)
        private String roadName;        // 도로명 주소
        private String firstBuildNo;    // 건물번호1
        private String secondBuildNo;   // 건물번호2
    }
}
