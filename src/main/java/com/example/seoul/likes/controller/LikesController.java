package com.example.seoul.likes.controller;

import com.example.seoul.common.ApiResponse;
import com.example.seoul.common.LoginCheck;
import com.example.seoul.common.SuccessMessage;
import com.example.seoul.domain.User;
import com.example.seoul.likes.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikesController {
    private final LikesService likesService;

    @PostMapping("/{postId}")
    @LoginCheck
    public ResponseEntity<ApiResponse<Void>> likePost(
            @PathVariable Long postId,
            @SessionAttribute("user") User user) {
        likesService.likePost(user.getId(), postId);
        return ApiResponse.success(SuccessMessage.SUCCESS_LIKE_POST);
    }

    @DeleteMapping("/{postId}")
    @LoginCheck
    public ResponseEntity<ApiResponse<Void>> unlikePost(
            @PathVariable Long postId,
            @SessionAttribute("user") User user) {
        likesService.unlikePost(user.getId(), postId);
        return ApiResponse.success(SuccessMessage.SUCCESS_UNLIKE_POST);
    }
}
