package com.example.seoul.user.controller;

import com.example.seoul.common.LoginCheck;
import com.example.seoul.user.request.LoginRequest;
import com.example.seoul.user.request.SignUpRequest;
import com.example.seoul.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(
            @RequestBody SignUpRequest request
            ) {
        userService.signUp(request.toEntity());
        return ResponseEntity.ok("성공");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @RequestBody LoginRequest request,
            HttpSession session
            ) {
        userService.login(request.toEntity());
        session.setAttribute("user", request.toEntity());
        return ResponseEntity.ok("성공");
    }

    @PostMapping("/logout")
    @LoginCheck
    public ResponseEntity<Object> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("성공");
    }


}
