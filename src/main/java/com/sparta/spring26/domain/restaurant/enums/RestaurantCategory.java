package com.sparta.spring26.domain.restaurant.enums;

public enum RestaurantCategory {
    KOREAN(Category.KOREAN),   // 한식
    CHINESE(Category.CHINESE),   // 중식
    JAPANESE(Category.JAPANESE),    // 일식
    WESTERN(Category.WESTERN),   // 양식
    FAST_FOOD(Category.FAST_FOOD),    // 페스트 푸드
    CAFE(Category.CAFE),    // 카페
    OTHER(Category.OTHER);

    private final String category;

    RestaurantCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }

    public static class Category {
        public static final String KOREAN = "KOREAN";
        public static final String CHINESE = "CHINESE";
        public static final String JAPANESE = "JAPANESE";
        public static final String WESTERN = "WESTERN";
        public static final String FAST_FOOD = "FAST_FOOD";
        public static final String CAFE = "CAFE";
        public static final String OTHER = "OTHER";
    }
}
