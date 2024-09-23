package com.sparta.spring26.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewRequestDto {

    @NotBlank(message = "리뷰 내용은 비워둘 수 있습니다.")
    private String contents;

    @NotNull(message = "Rating은 필수 값입니다.")
    @Min(value = 1, message = "Rating은 최소 1이어야 합니다.")
    @Max(value = 5, message = "Rating은 최대 5이여야 합니다.")
    private Integer rating;

    @NotNull(message = "메뉴 ID는 필수 값입니다.")
    private Long menuId;

    @NotNull(message = "레스토랑 ID는 필수 값입니다.")
    private Long restaurantId;
}
