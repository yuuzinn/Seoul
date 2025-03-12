package com.example.seoul.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    private String profileImage;

    private Boolean isKakaoUser;

    // 생성자 (필수 필드만)
    @Builder
    public User(String username, String password, String nickname, String email, String profileImage, Boolean isKakaoUser) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.isKakaoUser = isKakaoUser;
    }

    // 닉네임 수정 등 메서드 추가 가능!
}


