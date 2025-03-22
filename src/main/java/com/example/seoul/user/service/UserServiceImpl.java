package com.example.seoul.user.service;

import com.example.seoul.domain.User;
import com.example.seoul.exception.CustomException;
import com.example.seoul.exception.ErrorCode;
import com.example.seoul.user.repository.UserRepository;
import com.example.seoul.user.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void signUp(User request) {
        Optional<User> byEmail = userRepository.findByEmail(request.getEmail());
        if (byEmail.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_USER);
        }
        String passwordEncode = passwordEncoder.encode(request.getPassword());
        User userSave = User.builder()
                .email(request.getEmail())
                .username(request.getUserName())
                .isKakaoUser(request.getIsKakaoUser())
                .nickname(request.getNickname())
                .profileImage(request.getProfileImage())
                .password(passwordEncode)
                .build();
        userRepository.save(userSave);
    }

    @Override
    public User login(User request) {
        String requestPassword = request.getPassword();
        Optional<User> user = userRepository.findByUserName(request.getUserName());
        if (user.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
        String dbPassword = user.get().getPassword();
        if (!isSamePassword(requestPassword, dbPassword)) {
            throw new CustomException(ErrorCode.NOT_SAME_PASSWORD);
        }
        return user.get();
    }

    @Override
    @Transactional
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedPassword);
    }


    public boolean isSamePassword(String password, String dbUserPassword) {
        return passwordEncoder.matches(password, dbUserPassword);
    }
}
