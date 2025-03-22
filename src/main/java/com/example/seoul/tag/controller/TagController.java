package com.example.seoul.tag.controller;

import com.example.seoul.common.ApiResponse;
import com.example.seoul.common.SuccessMessage;
import com.example.seoul.domain.type.TagType;
import com.example.seoul.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<String>>> getTags(@RequestParam TagType type) {
        List<String> tags = tagService.getTagsByType(type);
        return ApiResponse.success(SuccessMessage.SUCCESS_GET_TAGS, tags);
    }
}
