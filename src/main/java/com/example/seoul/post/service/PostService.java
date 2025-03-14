package com.example.seoul.post.service;

import com.example.seoul.post.dto.PostRequest;
import org.springframework.stereotype.Service;

@Service
public interface PostService {
    Long createPost(PostRequest request, Long userId);

}
