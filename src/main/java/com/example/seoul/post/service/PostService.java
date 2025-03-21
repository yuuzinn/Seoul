package com.example.seoul.post.service;

import com.example.seoul.post.dto.PostDetailResponse;
import com.example.seoul.post.dto.PostListResponse;
import com.example.seoul.post.dto.PostRequest;
import com.example.seoul.post.dto.PostResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    Long createPost(PostRequest request, Long userId);

    void updatePost(Long postId, Long userId, PostRequest request);

    PostDetailResponse getPostDetail(Long postId);

    PostListResponse getPosts(String subwayTag, Long lastPostId, int size);

    void deletePost(Long postId, Long userId);

    List<PostResponse> getMyPosts(Long userId, Long lastId);

    List<PostResponse> getLikedPosts(Long userId, Long lastId);

    PostListResponse searchPosts(String subwayTag, List<String> tags, String sort, Long lastPostId, int size);
}
