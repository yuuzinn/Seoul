package com.example.seoul.post.controller;

import com.example.seoul.common.LoginCheck;
import com.example.seoul.domain.User;
import com.example.seoul.post.dto.PostRequest;
import com.example.seoul.post.service.PostService;
import com.example.seoul.user.request.LoginRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @PostMapping
    @LoginCheck
    public ResponseEntity<Object> createPost(
            @RequestBody @Valid PostRequest request,
            HttpSession session) {

        User loginUser = (User) session.getAttribute("user");
        Long userId = loginUser.getId();

        Long postId = postService.createPost(request, userId);
        return ResponseEntity.ok(postId);
    }
}
