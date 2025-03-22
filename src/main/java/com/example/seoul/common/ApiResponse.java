package com.example.seoul.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    private final boolean success;
    private final int code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T result;

    // 데이터 없는 성공 응답
    public static ResponseEntity<ApiResponse<Void>> success(SuccessMessage message) {
        return ResponseEntity.ok(new ApiResponse<>(true, message.getCode(), message.getMessage(), null));
    }

    // 데이터 있는 성공 응답
    public static <T> ResponseEntity<ApiResponse<T>> success(SuccessMessage message, T result) {
        return ResponseEntity.ok(new ApiResponse<>(true, message.getCode(), message.getMessage(), result));
    }
}
