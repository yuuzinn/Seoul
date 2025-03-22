package com.example.seoul.user.controller;

import com.example.seoul.client.EmailAuthClient;
import com.example.seoul.user.request.CodeVerificationRequest;
import com.example.seoul.user.request.EmailOnlyRequest;
import com.example.seoul.user.request.ResetPasswordRequest;
import com.example.seoul.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class EmailAuthController {

    private final EmailAuthClient emailAuthClient;
    private final UserService userService;

    @PostMapping("/send-code")
    public ResponseEntity<?> sendResetCode(@RequestBody EmailOnlyRequest request) {
        emailAuthClient.sendAuthCode(request.getEmail());
        return ResponseEntity.ok("인증 코드가 전송되었습니다.");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody CodeVerificationRequest request) {
        boolean result = emailAuthClient.verifyCode(request.getEmail(), request.getCode());
        if (result) {
            return ResponseEntity.ok("인증 성공");
        } else {
            return ResponseEntity.status(400).body("인증 실패: 올바르지 않거나 만료된 코드입니다.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean verified = emailAuthClient.verifyCode(request.getEmail(), request.getCode());
        if (!verified) {
            return ResponseEntity.badRequest().body("인증 코드가 유효하지 않습니다.");
        }
        userService.updatePassword(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}


