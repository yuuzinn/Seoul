package com.example.seoul.likes.service;

import org.springframework.stereotype.Service;

@Service
public interface LikesService {
    void likePost(Long userId, Long postId);
    void unlikePost(Long userId, Long postId);
}
