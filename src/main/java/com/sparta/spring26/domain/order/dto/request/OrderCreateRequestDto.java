package com.sparta.spring26.domain.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestDto {

    @NotNull(message = "레스토랑 ID는 필수입니다.")
    private Long restaurantId;

    @NotNull(message = "메뉴 ID는 필수입니다.")
    private List<Long> menuIds;

    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    private Integer quantity;

    private List<MenuOrderRequestDto> menuOrderList;
}