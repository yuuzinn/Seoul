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
        private List<Poi> pois;

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
            private String buildingNo1;     // 건물번호1
            private String buildingNo2;     // 건물번호2
        }
    }
}
