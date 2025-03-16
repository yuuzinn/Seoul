package com.example.seoul.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostListResponse {
    private List<PostSummaryResponse> posts;  // 글 카드 리스트
    private boolean hasNext;                  // 다음 데이터 존재 여부
}

