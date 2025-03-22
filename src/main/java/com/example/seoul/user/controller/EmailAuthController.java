package com.example.seoul.user.controller;

import com.example.seoul.client.EmailAuthClient;
import com.example.seoul.common.ApiResponse;
import com.example.seoul.common.SuccessMessage;
import com.example.seoul.exception.CustomException;
import com.example.seoul.exception.ErrorCode;
import com.example.seoul.user.request.CodeVerificationRequest;
import com.example.seoul.user.request.EmailOnlyRequest;
import com.example.seoul.user.request.ResetPasswordRequest;
import com.example.seoul.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class EmailAuthController {

    private final EmailAuthClient emailAuthClient;
    private final UserService userService;

    @PostMapping("/send-code")
    public ResponseEntity<?> sendResetCode(@RequestBody EmailOnlyRequest request) {
        emailAuthClient.sendAuthCode(request.getEmail());
        return ApiResponse.success(SuccessMessage.SUCCESS_SEND_CODE);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody CodeVerificationRequest request) {
        boolean result = emailAuthClient.verifyCode(request.getEmail(), request.getCode());
        if (!result) {
            throw new CustomException(ErrorCode.INVALID_AUTH_TOKEN); // 또는 적절한 인증 실패용 에러코드
        }
        return ApiResponse.success(SuccessMessage.SUCCESS_VERIFY_CODE);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean verified = emailAuthClient.verifyCode(request.getEmail(), request.getCode());
        if (!verified) {
            throw new CustomException(ErrorCode.INVALID_AUTH_TOKEN);
        }
        userService.updatePassword(request.getEmail(), request.getNewPassword());
        return ApiResponse.success(SuccessMessage.SUCCESS_RESET_PASSWORD);
    }
}
