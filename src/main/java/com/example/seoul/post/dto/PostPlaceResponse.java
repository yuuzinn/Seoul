package com.example.seoul.post.dto;

import com.example.seoul.domain.PostPlace;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostPlaceResponse {
    private int placeOrder;
    private String placeName;
    private String description;
    private String imageUrl;
    private double latitude;
    private double longitude;

    public PostPlaceResponse(PostPlace postPlace) {
        this.placeOrder = postPlace.getPlaceOrder();
        this.placeName = postPlace.getPlaceName();
        this.description = postPlace.getDescription();
        this.imageUrl = postPlace.getImageUrl();
        this.latitude = postPlace.getLatitude();
        this.longitude = postPlace.getLongitude();
    }
}

