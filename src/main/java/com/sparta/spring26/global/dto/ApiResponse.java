package com.sparta.spring26.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private final String message;
    private final Integer statusCode;
    private final T data;


    /**
     * 성공 응답
     */
    public static <T> ApiResponse<T> success(String message, Integer statusCode, T data) {
        return new ApiResponse<T>(message, statusCode, data);
    }

    /**
     * 에러 응답
     */
    public static <T> ApiResponse<T> createError(String message, Integer statusCode) {
        return new ApiResponse<T>(message, statusCode, null);
    }
}