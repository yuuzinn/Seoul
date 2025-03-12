package com.example.seoul.user.request;

import com.example.seoul.domain.User;
import lombok.Getter;

@Getter
public class LoginRequest {
    private String userName;
    private String password;

    public User toEntity() {
        return User.builder()
                .username(this.userName)
                .password(this.password)
                .build();
    }
}
