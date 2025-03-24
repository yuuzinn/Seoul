package com.example.seoul.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepository {
    // userId → 해당 유저의 SseEmitter 저장소
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    // 서버 기동 시 초기화
    public void init() {
        emitters.clear();
    }

    // Emitter 저장
    public void save(Long userId, SseEmitter emitter) {
        emitters.put(userId, emitter);
    }

    // Emitter 조회
    public SseEmitter get(Long userId) {
        return emitters.get(userId);
    }

    // Emitter 삭제
    public void delete(Long userId) {
        emitters.remove(userId);
    }
}
