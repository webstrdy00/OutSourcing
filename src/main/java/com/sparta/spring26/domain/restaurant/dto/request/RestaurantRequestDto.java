package com.sparta.spring26.domain.restaurant.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class RestaurantRequestDto {
    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "설명은 필수입니다.")
    private String description;

    @NotNull(message = "오픈 시간은 필수입니다.")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;

    @NotNull(message = "마감 시간은 필수입니다.")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    @NotNull(message = "최소 배달 가격은 필수입니다.")
    private Integer minDeliveryPrice;

    @NotBlank(message = "주소는 필수입니다.")
    private String address;
}
