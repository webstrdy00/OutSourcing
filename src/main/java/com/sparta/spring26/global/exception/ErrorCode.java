package com.sparta.spring26.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 유저 에러메세지
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    NOT_OWNER(HttpStatus.FORBIDDEN, "User is not an owner"),

    // 가게 에러 메세지
    MAX_RESTAURANT_LIMIT(HttpStatus.BAD_REQUEST, "Owner has reached the maximum limit of restaurants"),
    RESTAURANT_NOT_FOUND(HttpStatus.NOT_FOUND, "Restaurant not found"),
    NOT_RESTAURANT_OWNER(HttpStatus.FORBIDDEN, "User is not the owner of this restaurant"),

    // 주문 에러 메시지
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 주문을 찾을 수 없습니다."),
    ORDER_NOT_CANCELLABLE(HttpStatus.BAD_REQUEST, "주문 중인 상태에서만 삭제할 수 있습니다."),

    // 리뷰 에러 메세지
    REVIEW_NOT_FOUND((HttpStatus.NOT_FOUND"해당 리뷰를 찾을 수 없습니다."),
    REVIEW_NOT_AUTHORIZED(HttpStatus.FORBIDDEN,"해당 리뷰에 대한 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
