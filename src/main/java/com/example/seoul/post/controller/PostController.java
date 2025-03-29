package com.example.seoul.post.controller;

import com.example.seoul.common.ApiResponse;
import com.example.seoul.common.LoginCheck;
import com.example.seoul.common.SuccessMessage;
import com.example.seoul.domain.User;
import com.example.seoul.post.dto.*;
import com.example.seoul.post.service.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;

    @PostMapping
    @LoginCheck
    public ResponseEntity<ApiResponse<Long>> createPost(
            @RequestBody @Valid PostRequest request,
            HttpSession session) {

        User loginUser = (User) session.getAttribute("user");
        Long postId = postService.createPost(request, loginUser.getId());

        return ApiResponse.success(SuccessMessage.SUCCESS_CREATE_POST, postId);
    }

    @PutMapping("/{postId}")
    @LoginCheck
    public ResponseEntity<ApiResponse<Void>> updatePost(
            @PathVariable Long postId,
            @RequestBody @Valid PostRequest request,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        postService.updatePost(postId, user.getId(), request);

        return ApiResponse.success(SuccessMessage.SUCCESS_UPDATE_POST);
    }

    @DeleteMapping("/{postId}")
    @LoginCheck
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long postId,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        postService.deletePost(postId, user.getId());

        return ApiResponse.success(SuccessMessage.SUCCESS_DELETE_POST);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> getPostDetail(
            @PathVariable Long postId) {

        return ApiResponse.success(SuccessMessage.SUCCESS_GET_POST_DETAIL, postService.getPostDetail(postId));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PostListResponse>> getPosts(
            @RequestParam String subway,
            @RequestParam(required = false) Long lastPostId,
            @RequestParam(defaultValue = "1") int size) {

        return ApiResponse.success(SuccessMessage.SUCCESS_GET_POSTS, postService.getPosts(subway, lastPostId, size));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PostListResponse>> searchPosts(
            @RequestParam String subway,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false, defaultValue = "latest") String sort,
            @RequestParam(required = false) Long lastPostId,
            @RequestParam(defaultValue = "10") int size) {

        return ApiResponse.success(SuccessMessage.SUCCESS_SEARCH_POSTS,
                postService.searchPosts(subway, tags, sort, lastPostId, size));
    }

    @GetMapping("/mine")
    @LoginCheck
    public ResponseEntity<ApiResponse<List<PostResponse>>> getMyPosts(
            @RequestParam(required = false) Long lastId,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        return ApiResponse.success(SuccessMessage.SUCCESS_GET_MY_POSTS,
                postService.getMyPosts(user.getId(), lastId));
    }

    @GetMapping("/liked")
    @LoginCheck
    public ResponseEntity<ApiResponse<List<PostResponse>>> getLikedPosts(
            @RequestParam(required = false) Long lastId,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        return ApiResponse.success(SuccessMessage.SUCCESS_GET_LIKED_POSTS,
                postService.getLikedPosts(user.getId(), lastId));
    }
}
