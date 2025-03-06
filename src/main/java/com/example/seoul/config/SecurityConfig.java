package com.example.seoul.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (테스트용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html",
                                "/auth/login/kakao", "/auth/session",
                                "/auth/kakao-login-url").permitAll() // 로그인 관련 URL 허용
                        .anyRequest().authenticated() // 나머지는 인증 필요
                )
                .formLogin((formLogin) -> formLogin.defaultSuccessUrl("/"))
                .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true));

        return http.build();
    }
}


