package com.example.seoul.place.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostPlaceRequest {
    private String placeName;
    private double latitude;
    private double longitude;
    private String description;
    private String imageUrl;  // 프론트에서 이미지 업로드 후 URL 전달
    private int placeOrder;   // 사용자가 직접 순서 지정
}
