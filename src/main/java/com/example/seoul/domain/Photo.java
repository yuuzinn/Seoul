package com.example.seoul.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Review review;

    @Column(nullable = false)
    private String imageUrl;

    private String fileName;

    private LocalDateTime uploadedAt;

    @Builder
    public Photo(Review review, String imageUrl, String fileName) {
        this.review = review;
        this.imageUrl = imageUrl;
        this.fileName = fileName;
        this.uploadedAt = LocalDateTime.now();
    }
}

