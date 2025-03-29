package com.example.seoul.pedestrian.controller;

import com.example.seoul.common.ApiResponse;
import com.example.seoul.common.SuccessMessage;
import com.example.seoul.pedestrian.dto.PedestrianRouteResponse;
import com.example.seoul.pedestrian.dto.TmapPedestrianRouteRequest;
import com.example.seoul.pedestrian.service.PedestrianService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PedestrianController {

    private final PedestrianService pedestrianService;

    @PostMapping("/pedestrian-route")
    public ResponseEntity<ApiResponse<PedestrianRouteResponse>> getPedestrianRoute(
            @RequestBody TmapPedestrianRouteRequest request) {
        PedestrianRouteResponse response = pedestrianService.getPedestrianRoute(request);
        return ApiResponse.success(SuccessMessage.SUCCESS_GET_PEDESTRIAN_ROUTE, response);
    }
}
