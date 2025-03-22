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
    User login(User request);


    /**
     * 비밀번호 변경
     */
    void updatePassword(String email, String newPassword);
}
