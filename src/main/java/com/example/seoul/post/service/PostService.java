package com.example.seoul.post.service;

import com.example.seoul.post.dto.PostDetailResponse;
import com.example.seoul.post.dto.PostListResponse;
import com.example.seoul.post.dto.PostRequest;
import org.springframework.stereotype.Service;

@Service
public interface PostService {
    Long createPost(PostRequest request, Long userId);

    void updatePost(Long postId, Long userId, PostRequest request);

    PostDetailResponse getPostDetail(Long postId);

    PostListResponse getPosts(String subwayTag, Long lastPostId, int size);

    void deletePost(Long postId, Long userId);
}
