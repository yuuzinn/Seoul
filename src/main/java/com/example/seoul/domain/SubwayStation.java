package com.example.seoul.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubwayStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String stationId;  // BLDN_ID (고유 코드)

    @Column(nullable = false)
    private String stationName;  // BLDN_NM (지하철역 이름)

    @Column(nullable = false)
    private String lineNumber;  // ROUTE (호선)

    @Column(nullable = false)
    private double latitude;  // LAT (위도)

    @Column(nullable = false)
    private double longitude;  // LOT (경도)
}

