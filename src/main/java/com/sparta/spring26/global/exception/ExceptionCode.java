package com.sparta.spring26.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
    RESTAURANT_NOT_FOUND("가게를 찾을 수 없습니다."),
    MENU_NOT_FOUND("메뉴를 찾을 수 없습니다."),
    RESTAURANT_MENU_MISMATCH("가게에 포함된 메뉴가 아닙니다."),
    RESTAURANT_OWNER_MISMATCH("가게의 사장님만 가능합니다."),
    RESTAURANT_NOT_OPEN("가게 오픈 시간이 아닙니다."),

    // 주문 에러 메시지
    ORDER_NOT_FOUND("해당 주문을 찾을 수 없습니다."),
    ORDER_NOT_CANCELLABLE("주문 중인 상태에서만 삭제할 수 있습니다."),
    ORDER_RECORD_NOT_FOUND("해당 메뉴에 대한 주문 기록이 없습니다."),


    // 리뷰 에러 메세지
    REVIEW_NOT_FOUND("해당 리뷰를 찾을 수 없습니다."),
    REVIEW_NOT_AUTHORIZED("해당 리뷰에 대한 권한이 없습니다."),
    REVIEW_CANNOT_BE_CREATED("리뷰는 배달 완료 상태에서만 작성할 수 있습니다."),
    REVIEW_PERMISSION_DENIED("해당 주문에 대한 권한이 없습니다."),

    // 장바구니 에러 메세지
    CART_NOT_FOUND("장바구니에 담겨져 있는 음식이 없습니다."),
    CART_EXPIRED("장바구니 보관 시간이 만료되었습니다.");


    private final String message;
}
