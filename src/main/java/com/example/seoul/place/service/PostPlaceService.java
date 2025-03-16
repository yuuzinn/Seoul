package com.example.seoul.place.service;

import com.example.seoul.place.dto.PoiSearchResponseDto;
import com.example.seoul.place.dto.PostPlaceRequest;
import org.springframework.stereotype.Service;

@Service
public interface PostPlaceService {
    PoiSearchResponseDto searchPoi(String keyword, int count);
}
