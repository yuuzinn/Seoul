package com.example.seoul.likes.controller;

import com.example.seoul.common.LoginCheck;
import com.example.seoul.domain.User;
import com.example.seoul.likes.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class LikesController {
    private final LikesService likesService;

    @PostMapping("/{postId}/like")
    @LoginCheck
    public ResponseEntity<Void> likePost(
            @PathVariable Long postId,
            @SessionAttribute("user") User user) {
        likesService.likePost(user.getId(), postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/like")
    @LoginCheck
    public ResponseEntity<Void> unlikePost(
            @PathVariable Long postId,
            @SessionAttribute("user") User user) {
        likesService.unlikePost(user.getId(), postId);
        return ResponseEntity.ok().build();
    }
}

