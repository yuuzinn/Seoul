package com.example.seoul.post.dto;

import com.example.seoul.domain.Post;
import lombok.Getter;

import java.util.List;

@Getter
public class PostSummaryResponse {
    private Long postId;
    private String title;
    private String subwayTag;
    private String thumbnailImageUrl;
    private List<String> moodTags;
    private int likeCount;

    public PostSummaryResponse(Post post, String subwayTag, String thumbnail, List<String> moodTags) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.subwayTag = subwayTag;
        this.thumbnailImageUrl = thumbnail;
        this.moodTags = moodTags;
        this.likeCount = post.getLikeCount();
    }
}
