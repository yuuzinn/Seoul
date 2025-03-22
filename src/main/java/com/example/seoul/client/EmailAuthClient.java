package com.example.seoul.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailAuthClient {

    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender mailSender;

    private static final String EMAIL_AUTH_PREFIX = "email-auth:";
    private static final long TIME_LIMIT = 5 * 60; // 5분

    public void sendAuthCode(String toEmail) {
        String code = generateRandomCode();
        redisTemplate.opsForValue().set(
                EMAIL_AUTH_PREFIX + toEmail,
                code,
                Duration.ofSeconds(TIME_LIMIT)
        );
        sendEmail(toEmail, code);
    }

    public boolean verifyCode(String email, String code) {
        String key = EMAIL_AUTH_PREFIX + email;
        String stored = redisTemplate.opsForValue().get(key);
        return stored != null && stored.equals(code);
    }

    private String generateRandomCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private void sendEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[서울 코스추천] 비밀번호 재설정 인증 코드");
        message.setText("아래 인증 코드를 입력해주세요.\n\n인증코드: " + code + "\n(유효시간: 5분)");
        mailSender.send(message);
    }
}


