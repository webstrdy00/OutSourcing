package com.sparta.spring26.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    RESTAURANT_NOT_FOUND("가게를 찾을 수 없습니다."),
    MENU_NOT_FOUND("메뉴를 찾을 수 없습니다."),
    RESTAURANT_MENU_MISMATCH("가게에 포함된 메뉴가 아닙니다."),
    RESTAURANT_OWNER_MISMATCH("가게의 사장님만 가능합니다.");

    private final String message;
}
