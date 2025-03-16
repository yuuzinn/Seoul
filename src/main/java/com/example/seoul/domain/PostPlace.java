package com.example.seoul.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Column(nullable = false, length = 100)
    private String placeName;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = true, length = 1000)
    private String description;

    private String imageUrl;

    private int placeOrder; // 경로 순서 지정용

    @Builder
    public PostPlace(Post post, String placeName, double latitude, double longitude, String description, String imageUrl, int placeOrder) {
        this.post = post;
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.imageUrl = imageUrl;
        this.placeOrder = placeOrder;
    }

    public void update(String placeName, double latitude, double longitude, String description, String imageUrl, int placeOrder) {
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.imageUrl = imageUrl;
        this.placeOrder = placeOrder;
    }

}
