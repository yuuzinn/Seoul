package com.example.seoul.util.kakao;

import com.example.seoul.domain.User;
import com.example.seoul.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoOAuthService kakaoOAuthService;
    private final UserRepository userRepository;

    public User kakaoLogin(String code, HttpSession session) {
        String accessToken = kakaoOAuthService.getAccessToken(code);

        Map<String, Object> userInfo = kakaoOAuthService.getUserInfo(accessToken);
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");
        String profileImage = (String) profile.get("profile_image_url");

        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user;

        if (optionalUser.isEmpty()) {
            user = User.builder()
                    .email(email)
                    .nickname(nickname)
                    .profileImage(profileImage)
                    .isKakaoUser(true)
                    .build();
            userRepository.save(user);
        } else {
            user = optionalUser.get();
        }

        session.setAttribute("user", user);

        return user;
    }
}
