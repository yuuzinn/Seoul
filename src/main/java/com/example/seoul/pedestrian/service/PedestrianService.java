package com.example.seoul.pedestrian.service;

import com.example.seoul.client.TmapPedestrianRouteClient;
import com.example.seoul.exception.CustomException;
import com.example.seoul.exception.ErrorCode;
import com.example.seoul.pedestrian.dto.PedestrianRouteResponse;
import com.example.seoul.pedestrian.dto.TmapPedestrianRouteRequest;
import com.example.seoul.pedestrian.dto.TmapPedestrianRouteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedestrianService {

    private final TmapPedestrianRouteClient tmapPedestrianRouteClient;

    public PedestrianRouteResponse getPedestrianRoute(TmapPedestrianRouteRequest request) {
        TmapPedestrianRouteResponse tmapResponse = tmapPedestrianRouteClient.getPedestrianRoute(request);

        int totalDistance = tmapResponse.getFeatures().get(0).getProperties().getTotalDistance();
        int totalTime = tmapResponse.getFeatures().get(0).getProperties().getTotalTime();

        List<PedestrianRouteResponse.PointRoute> pointRoutes = tmapResponse.getFeatures().stream()
                .filter(feature -> "Point".equals(feature.getGeometry().getType()))
                .map(feature -> {
                    Object coords = feature.getGeometry().getCoordinates();
                    double longitude, latitude;
                    if (coords instanceof List<?> list) {
                        if (!list.isEmpty() && list.get(0) instanceof List<?>) {
                            List<Double> pointCoords = (List<Double>) ((List<?>) list).get(0);
                            longitude = pointCoords.get(0);
                            latitude = pointCoords.get(1);
                        } else {
                            List<Double> pointCoords = (List<Double>) list;
                            longitude = pointCoords.get(0);
                            latitude = pointCoords.get(1);
                        }
                    } else {
                        throw new CustomException(ErrorCode.INVALID_COORDINATE_FORMAT_IN_RESPONSE);
                    }
                    return new PedestrianRouteResponse.PointRoute(
                            latitude,
                            longitude,
                            feature.getProperties().getDescription()
                    );
                })
                .collect(Collectors.toList());

        List<PedestrianRouteResponse.LineRoute> lineRoutes = tmapResponse.getFeatures().stream()
                .filter(feature -> "LineString".equals(feature.getGeometry().getType()))
                .map(feature -> {
                    List<List<Double>> coordinates = (List<List<Double>>) feature.getGeometry().getCoordinates();
                    return new PedestrianRouteResponse.LineRoute(
                            coordinates,
                            feature.getProperties().getDistance() != null ? feature.getProperties().getDistance() : 0,
                            feature.getProperties().getTime() != null ? feature.getProperties().getTime() : 0,
                            feature.getProperties().getName(),
                            feature.getProperties().getDescription(),
                            feature.getProperties().getRoadType() != null ? feature.getProperties().getRoadType() : 0,
                            feature.getProperties().getCategoryRoadType() != null ? feature.getProperties().getCategoryRoadType() : 0
                    );
                })
                .collect(Collectors.toList());

        return new PedestrianRouteResponse(totalDistance, totalTime, pointRoutes, lineRoutes);
    }
}
