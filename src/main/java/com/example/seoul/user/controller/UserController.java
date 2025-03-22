package com.example.seoul.user.controller;

import com.example.seoul.common.ApiResponse;
import com.example.seoul.common.LoginCheck;
import com.example.seoul.common.SuccessMessage;
import com.example.seoul.domain.User;
import com.example.seoul.user.request.LoginRequest;
import com.example.seoul.user.request.SignUpRequest;
import com.example.seoul.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        userService.signUp(request.toEntity());
        return ApiResponse.success(SuccessMessage.SUCCESS_SIGNUP);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        User user = userService.login(request.toEntity());
        session.setAttribute("user", user);
        return ApiResponse.success(SuccessMessage.SUCCESS_LOGIN);
    }

    @PostMapping("/logout")
    @LoginCheck
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ApiResponse.success(SuccessMessage.SUCCESS_LOGOUT);
    }
}
