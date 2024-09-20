package com.sparta.spring26.domain.restaurant.enums;

public enum RestaurantStatus {
    OPEN(Status.OPEN),
    CLOSED(Status.CLOSED),
    TEMPORARILY_CLOSED(Status.TEMPORARILY_CLOSED);

    private final String status;

    RestaurantStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public static class Status {
        public static final String OPEN = "OPEN";
        public static final String CLOSED = "CLOSED";
        public static final String TEMPORARILY_CLOSED = "TEMPORARILY_CLOSED";
    }
}
