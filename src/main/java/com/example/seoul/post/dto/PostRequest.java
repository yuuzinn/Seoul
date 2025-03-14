package com.example.seoul.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private List<String> subwayTags;

    private List<String> moodTags;
}
