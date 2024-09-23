package com.sparta.spring26.domain.user.enums;

public enum UserStatus {
    ACTIVE(Status.ACTIVE),    // 정상 사용자
    INACTIVE(Status.INACTIVE),  // 탈퇴 사용자
    SUSPENDED(Status.SUSPENDED);   // 일시 정지된 사용자

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public static class Status {
        public static final String ACTIVE = "ACTIVE";
        public static final String INACTIVE = "INACTIVE";
        public static final String SUSPENDED = "SUSPENDED";
    }
}
