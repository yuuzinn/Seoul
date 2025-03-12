package com.example.seoul.user.service;

import com.example.seoul.domain.User;
import com.example.seoul.user.request.SignUpRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    /**
     * 회원가입
     */
    void signUp(User request);

    /**
     * 로그인
     */
    void login(User request);


    /**
     * 회원탈퇴
     */
}
