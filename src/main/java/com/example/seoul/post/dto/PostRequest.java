package com.example.seoul.post.dto;

import com.example.seoul.place.dto.PostPlaceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String subwayTag;

    private List<String> moodTags;

    private List<PostPlaceRequest> postPlaces;
}
