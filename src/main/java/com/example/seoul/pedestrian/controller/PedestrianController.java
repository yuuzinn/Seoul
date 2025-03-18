package com.example.seoul.pedestrian.controller;

import com.example.seoul.pedestrian.dto.PedestrianRouteResponse;
import com.example.seoul.pedestrian.dto.TmapPedestrianRouteRequest;
import com.example.seoul.pedestrian.service.PedestrianService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tmap")
@RequiredArgsConstructor
public class PedestrianController {

    private final PedestrianService pedestrianService;

    @PostMapping("/pedestrian-route")
    public ResponseEntity<PedestrianRouteResponse> getPedestrianRoute(
            @RequestBody TmapPedestrianRouteRequest request) {
        return ResponseEntity.ok(pedestrianService.getPedestrianRoute(request));
    }
}
