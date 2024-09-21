package com.sparta.spring26.domain.order;

public enum OrderStatus {

    ORDER_RECEIVED(OrderStatus.Status.ORDER_RECEIVED), // 접수 중
    ORDER_ACCEPTED(OrderStatus.Status.ORDER_ACCEPTED), // 접수 완료
    COOKING(OrderStatus.Status.COOKING), // 조리 중
    COOKING_COMPLETED(OrderStatus.Status.COOKING_COMPLETED), // 조리 완료
    OUT_FOR_DELIVERY(OrderStatus.Status.OUT_FOR_DELIVERY), // 배달 중
    DELIVERYED(OrderStatus.Status.DELIVERYED); // 배달 완료


    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public static class Status {
        public static final String ORDER_RECEIVED = "ORDER_RECEIVED";
        public static final String ORDER_ACCEPTED = "ORDER_ACCEPTED";
        public static final String COOKING = "COOKING";
        public static final String COOKING_COMPLETED = "COOKING_COMPLETED";
        public static final String OUT_FOR_DELIVERY = "OUT_FOR_DELIVERY";
        public static final String DELIVERYED = "DELIVERYED";
    }

}
