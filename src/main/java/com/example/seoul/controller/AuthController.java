package com.example.seoul.controller;

import com.example.seoul.domain.User;
import com.example.seoul.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final AuthService authService;

    @GetMapping("/kakao-login-url")
    public String getKakaoLoginUrl() {
        return "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";
    }

    @GetMapping("/login/kakao")
    public RedirectView kakaoLogin(@RequestParam("code") String code, HttpServletRequest request) {
        HttpSession session = request.getSession();
        authService.kakaoLogin(code, session);
        return new RedirectView("/");
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new RedirectView("/");
    }

    @GetMapping("/session")
    public ResponseEntity<?> checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        System.out.println("session = " + session);
        if (session == null || session.getAttribute("loginUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 안됨");
        }
        User user = (User) session.getAttribute("loginUser");
        return ResponseEntity.ok().body(user);
    }
}
