package com.sparta.spring26.domain.order;

public enum OrderStatus {
    ORDER_RECEIVED, // 접수 중
    ORDER_ACCEPTED, // 접수 완료
    COOKING, // 조리 중
    COOKING_COMPLETED, // 조리 완료
    OUT_FOR_DELIVERY, // 배달 중
    DELIVERYED // 배달 완료
}
