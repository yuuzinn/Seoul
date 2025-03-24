package com.example.seoul.notification.repository;

import com.example.seoul.notification.domain.Notification;
import com.example.seoul.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 특정 사용자에 대한 알림 목록
    List<Notification> findByReceiverOrderByCreatedAtDesc(User receiver);

    // 읽지 않은 알림이 있는지 확인
    boolean existsByReceiverAndIsReadFalse(User receiver);
}
