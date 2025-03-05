package com.example.seoul.controller;

import com.example.seoul.service.SubwayStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subway")
public class SubwayStationController {

    private final SubwayStationService subwayStationService;

    @GetMapping("/fetch")
    public ResponseEntity<String> fetchSubwayStations() {
        try {
            subwayStationService.fetchAndSaveSubwayStations();
            return ResponseEntity.ok("지하철역 데이터 저장 완료!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("에러 발생: " + e.getMessage());
        }
    }
}
