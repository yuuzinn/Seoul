package com.example.seoul.user.request;

import com.example.seoul.domain.User;
import lombok.Getter;

@Getter
public class SignUpRequest {
    private String email;
    private Boolean isKakaoUser;
    private String nickname;
    private String password;
    private String profileImage;
    private String userName;

    public User toEntity() {
        return User.builder()
                .email(this.email)
                .isKakaoUser(false)
                .nickname(this.nickname)
                .password(this.password)
                .profileImage(this.profileImage)
                .username(this.userName)
                .build();
    }
}
