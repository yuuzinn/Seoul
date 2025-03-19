package com.example.seoul.subway.service;

import com.example.seoul.domain.SubwayStation;
import com.example.seoul.subway.repository.SubwayStationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubwayStationService {
    private final SubwayStationRepository subwayStationRepository;
    private final ObjectMapper objectMapper;

    @Value("${subway.api.key}")
    private String SUBWAY_KEY;

    private static final String API_URL = "http://openapi.seoul.go.kr:8088/%s/json/subwayStationMaster/1/1000/";

    public void fetchAndSaveSubwayStations() throws Exception {
        String formattedUrl = String.format(API_URL, SUBWAY_KEY);
        String json = getJsonFromApi(formattedUrl);
        List<SubwayStation> stations = parseJson(json);
        subwayStationRepository.saveAll(stations);
    }

    private String getJsonFromApi(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        return sb.toString();
    }

    private List<SubwayStation> parseJson(String json) throws Exception {
        JsonNode root = objectMapper.readTree(json);
        JsonNode rows = root.path("subwayStationMaster").path("row");

        List<SubwayStation> stationList = new ArrayList<>();
        for (JsonNode node : rows) {
            SubwayStation station = SubwayStation.builder()
                    .stationId(node.path("BLDN_ID").asText())
                    .stationName(node.path("BLDN_NM").asText())
                    .lineNumber(node.path("ROUTE").asText())
                    .latitude(node.path("LAT").asDouble())
                    .longitude(node.path("LOT").asDouble())
                    .build();
            stationList.add(station);
        }
        return stationList;
    }
}
