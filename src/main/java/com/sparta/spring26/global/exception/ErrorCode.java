package com.sparta.spring26.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    NOT_OWNER(HttpStatus.FORBIDDEN, "User is not an owner"),
    MAX_RESTAURANT_LIMIT(HttpStatus.BAD_REQUEST, "Owner has reached the maximum limit of restaurants"),
    RESTAURANT_NOT_FOUND(HttpStatus.NOT_FOUND, "Restaurant not found"),
    NOT_RESTAURANT_OWNER(HttpStatus.FORBIDDEN, "User is not the owner of this restaurant");

    private final HttpStatus httpStatus;
    private final String message;
}
