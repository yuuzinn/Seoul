package com.example.seoul.notification.controller;

import com.example.seoul.common.ApiResponse;
import com.example.seoul.common.LoginCheck;
import com.example.seoul.common.SuccessMessage;
import com.example.seoul.domain.User;
import com.example.seoul.notification.dto.NotificationResponse;
import com.example.seoul.notification.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/subscribe")
    @LoginCheck
    public SseEmitter subscribe(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return notificationService.subscribe(user.getId());
    }

    @GetMapping("/unread-exists")
    @LoginCheck
    public ResponseEntity<ApiResponse<Boolean>> hasUnreadNotification(HttpSession session) {
        User user = (User) session.getAttribute("user");
        boolean hasUnread = notificationService.hasUnread(user);
        return ApiResponse.success(SuccessMessage.SUCCESS_CHECK_UNREAD, hasUnread);
    }

    @GetMapping
    @LoginCheck
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getAllNotifications(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<NotificationResponse> notifications = notificationService.getAllNotifications(user);
        return ApiResponse.success(SuccessMessage.SUCCESS_GET_NOTIFICATIONS, notifications);
    }

    @PatchMapping("/{notificationId}/read")
    @LoginCheck
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable Long notificationId,
            HttpSession session
    ) {
        User user = (User) session.getAttribute("user");
        notificationService.markAsRead(user, notificationId);
        return ApiResponse.success(SuccessMessage.SUCCESS_READ_NOTIFICATION);
    }

}
