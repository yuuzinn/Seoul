package com.example.seoul.post.dto;

import com.example.seoul.domain.Post;
import lombok.Getter;

import java.util.List;

@Getter
public class PostDetailResponse {
    private Long postId;
    private String title;
    private String content;
    private String subwayTag;
    private List<String> moodTags;
    private List<PostPlaceResponse> places;

    public PostDetailResponse(Post post, String subwayTag, List<String> moodTags, List<PostPlaceResponse> places) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.subwayTag = subwayTag;
        this.moodTags = moodTags;
        this.places = places;
    }
}
