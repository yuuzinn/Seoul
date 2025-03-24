package com.example.seoul.notification.domain;

import com.example.seoul.domain.Post;
import com.example.seoul.domain.User;
import com.example.seoul.domain.type.NotificationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 알림 받는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    private User receiver;

    // 알림 발생시킨 사람 (좋아요 누른 사람)
    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;

    // 관련된 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    // 알림 메시지 (ex. "찡이님이 회원님의 게시글을 좋아합니다.")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    // 생성 시각
    private LocalDateTime createdAt;

    // 읽음 여부
    private boolean isRead;

    @Builder
    public Notification(User receiver, User sender, Post post, String message, NotificationType type) {
        this.receiver = receiver;
        this.sender = sender;
        this.post = post;
        this.type = type;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
