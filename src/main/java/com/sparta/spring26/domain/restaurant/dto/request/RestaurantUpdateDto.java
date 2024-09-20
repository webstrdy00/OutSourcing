package com.sparta.spring26.domain.restaurant.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class RestaurantUpdateDto {
    private String name;
    private String description;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Integer minDeliveryPrice;
    private String address;
}
