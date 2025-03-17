package com.example.seoul.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    /**
     * 잘못된 요청
     */
    INVALID_REQUEST(1000, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_INPUT_VALUE(1001, HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다."),
    /**
     * 인증 실패
     */
    UNAUTHORIZED(2000, HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    INVALID_AUTH_TOKEN(2001, HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 토큰입니다."),
    /**
     * 권한 없음
     */
    FORBIDDEN_ACCESS(3000, HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    /**
     * 리소스 없음
     */
    NOT_FOUND_USER(4000, HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    NOT_FOUND_POST(4001, HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    NOT_FOUND_PLACE(4002, HttpStatus.NOT_FOUND, "해당 장소를 찾을 수 없습니다."),
    /**
     * 서버 에러
     */
    INTERNAL_SERVER_ERROR(5000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    SERVICE_UNAVAILABLE(5001, HttpStatus.SERVICE_UNAVAILABLE, "일시적으로 사용할 수 없습니다.");

    private final int code;
    private final HttpStatus status;
    private final String message;

    ErrorCode(int code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
