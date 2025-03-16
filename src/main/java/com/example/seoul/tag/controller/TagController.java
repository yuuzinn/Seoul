package com.example.seoul.tag.controller;

import com.example.seoul.domain.type.TagType;
import com.example.seoul.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<String>> getTags(@RequestParam TagType type) {
        return ResponseEntity.ok(tagService.getTagsByType(type));
    }
}
