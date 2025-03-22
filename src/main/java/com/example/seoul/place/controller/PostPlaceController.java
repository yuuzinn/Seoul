package com.example.seoul.place.controller;

import com.example.seoul.common.ApiResponse;
import com.example.seoul.common.LoginCheck;
import com.example.seoul.common.SuccessMessage;
import com.example.seoul.place.dto.PoiSearchResponseDto;
import com.example.seoul.place.service.PostPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post-place")
public class PostPlaceController {

    private final PostPlaceService postPlaceService;

    @GetMapping("/search")
    @LoginCheck
    public ResponseEntity<ApiResponse<PoiSearchResponseDto>> searchPoi(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "5") int count
    ) {
        PoiSearchResponseDto result = postPlaceService.searchPoi(keyword, count);
        return ApiResponse.success(SuccessMessage.SUCCESS_SEARCH_POI, result);
    }
}
