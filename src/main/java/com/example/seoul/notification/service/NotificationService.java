package com.example.seoul.notification.service;

import com.example.seoul.domain.Post;
import com.example.seoul.domain.User;
import com.example.seoul.notification.dto.NotificationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Service
public interface NotificationService {
    /**
     * SSE 연결 요청
     */
    SseEmitter subscribe(Long userId);

    /**
     * 알림 호출 DB 저장
     */
    void notifyLike(User receiver, User sender, Post post);

    boolean hasUnread(User user);

    List<NotificationResponse> getAllNotifications(User user);

    void markAsRead(User user, Long notificationId);


}
