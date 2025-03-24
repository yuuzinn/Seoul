// ✅ 더미 이벤트 제거 (NotificationServiceImpl)

package com.example.seoul.notification.service;

import com.example.seoul.domain.Post;
import com.example.seoul.domain.User;
import com.example.seoul.domain.type.NotificationType;
import com.example.seoul.exception.CustomException;
import com.example.seoul.exception.ErrorCode;
import com.example.seoul.notification.domain.Notification;
import com.example.seoul.notification.dto.NotificationResponse;
import com.example.seoul.notification.repository.EmitterRepository;
import com.example.seoul.notification.repository.NotificationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 1시간

    @PostConstruct
    public void init() {
        emitterRepository.init();
    }

    @Override
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);

        log.info("SSE 연결됨: userId = {}", userId);

        emitter.onCompletion(() -> {
            emitterRepository.delete(userId);
            log.info("SSE 연결 종료: userId = {}", userId);
        });

        emitter.onTimeout(() -> {
            emitter.complete();
            emitterRepository.delete(userId);
            log.info("SSE 타임아웃 종료: userId = {}", userId);
        });

        return emitter;
    }

    @Transactional
    @Override
    public void notifyLike(User receiver, User sender, Post post) {
        if (receiver.getId().equals(sender.getId())) return;

        Notification notification = Notification.builder()
                .receiver(receiver)
                .sender(sender)
                .type(NotificationType.LIKE)
                .post(post)
                .message(sender.getNickname() + "님이 '" + post.getTitle() + "' 게시글에 좋아요를 누르었습니다.")
                .build();

        notificationRepository.save(notification);

        SseEmitter emitter = emitterRepository.get(receiver.getId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("like")
                        .data(notification.getMessage()));
                log.info("알림 전송 완료: to={}, message={}", receiver.getId(), notification.getMessage());
            } catch (IOException e) {
                emitterRepository.delete(receiver.getId());
                log.warn("알림 전송 실패: userId={}, error={}", receiver.getId(), e.getMessage());
            }
        }
    }

    @Override
    public boolean hasUnread(User user) {
        return notificationRepository.existsByReceiverAndIsReadFalse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getAllNotifications(User user) {
        return notificationRepository.findByReceiverOrderByCreatedAtDesc(user).stream()
                .map(NotificationResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public void markAsRead(User user, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_NOTIFICATION));

        if (!notification.getReceiver().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        notification.markAsRead();
    }

}
