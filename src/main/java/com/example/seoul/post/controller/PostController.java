package com.example.seoul.post.controller;

import com.example.seoul.common.LoginCheck;
import com.example.seoul.domain.Post;
import com.example.seoul.domain.User;
import com.example.seoul.post.dto.PostDetailResponse;
import com.example.seoul.post.dto.PostListResponse;
import com.example.seoul.post.dto.PostRequest;
import com.example.seoul.post.dto.PostResponse;
import com.example.seoul.post.service.PostService;
import com.example.seoul.user.request.LoginRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{postId}")
    @LoginCheck
    public ResponseEntity<Void> updatePost(@PathVariable Long postId,
                                           @RequestBody @Valid PostRequest request,
                                           HttpSession session) {
        User user = (User) session.getAttribute("user");
        postService.updatePost(postId, user.getId(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostDetail(postId));
    }

    @GetMapping
    public ResponseEntity<PostListResponse> getPosts(@RequestParam String subway,
                                                     @RequestParam(required = false) Long lastPostId,
                                                     @RequestParam(defaultValue = "1") int size) {
        return ResponseEntity.ok(postService.getPosts(subway, lastPostId, size));
    }

    @DeleteMapping("/{postId}")
    @LoginCheck
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        postService.deletePost(postId, user.getId());
        return ResponseEntity.ok().build();
    }


    @GetMapping("/mine")
    @LoginCheck
    public ResponseEntity<List<PostResponse>> getMyPosts(
            @RequestParam(required = false) Long lastId,
            HttpSession session
    ) {
        User user = (User) session.getAttribute("user");
        List<PostResponse> posts = postService.getMyPosts(user.getId(), lastId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/liked")
    @LoginCheck
    public ResponseEntity<List<PostResponse>> getLikedPosts(
            @RequestParam(required = false) Long lastId,
            HttpSession session
    ) {
        User user = (User) session.getAttribute("user");
        List<PostResponse> posts = postService.getLikedPosts(user.getId(), lastId);
        return ResponseEntity.ok(posts);
    }
}
