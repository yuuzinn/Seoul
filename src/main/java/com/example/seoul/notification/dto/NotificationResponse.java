package com.example.seoul.notification.dto;

import com.example.seoul.notification.domain.Notification;
import com.example.seoul.domain.type.NotificationType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResponse {
    private Long id;
    private String message;
    private boolean isRead;
    private NotificationType type;
    private LocalDateTime createdAt;

    private NotificationResponse(Long id, String message, boolean isRead, NotificationType type, LocalDateTime createdAt) {
        this.id = id;
        this.message = message;
        this.isRead = isRead;
        this.type = type;
        this.createdAt = createdAt;
    }

    public static NotificationResponse fromEntity(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getMessage(),
                notification.isRead(),
                notification.getType(),
                notification.getCreatedAt()
        );
    }
}
