package com.sparta.spring26.global.exception;

import com.sparta.spring26.global.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e) {
        return ApiResponse.createError(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }
}
