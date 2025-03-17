package com.example.seoul.common;

import com.example.seoul.exception.CustomException;
import com.example.seoul.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod && ((HandlerMethod) handler).hasMethodAnnotation(LoginCheck.class)) {
            HttpSession session = request.getSession();
            if (session == null || session.getAttribute("user") == null) {
                throw new CustomException(ErrorCode.NOT_LOGIN);
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
