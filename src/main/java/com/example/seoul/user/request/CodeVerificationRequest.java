package com.example.seoul.user.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CodeVerificationRequest {
    private String email;
    private String code;
}
