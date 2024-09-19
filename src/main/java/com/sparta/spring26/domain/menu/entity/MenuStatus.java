package com.sparta.spring26.domain.menu.entity;

import java.util.Arrays;

public enum MenuStatus {
    AVAILABLE,
    SOLD_OUT;

    public static MenuStatus of(String status) {
        return Arrays.stream(MenuStatus.values())
                .filter(e -> e.toString().equals(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid menu status: " + status));
    }
}