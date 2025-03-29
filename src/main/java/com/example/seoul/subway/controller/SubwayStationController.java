package com.example.seoul.subway.controller;

import com.example.seoul.common.ApiResponse;
import com.example.seoul.common.SuccessMessage;
import com.example.seoul.subway.service.SubwayStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subway")
public class SubwayStationController {

    private final SubwayStationService subwayStationService;

    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse<Void>> fetchSubwayStations() throws Exception {
        subwayStationService.fetchAndSaveSubwayStations();
        return ApiResponse.success(SuccessMessage.SUCCESS_SUBWAY_FETCH);
    }
}
