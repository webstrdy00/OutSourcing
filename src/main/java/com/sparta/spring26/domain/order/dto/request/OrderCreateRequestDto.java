package com.sparta.spring26.domain.order.dto.request;

import lombok.Getter;
import lombok.Setter;

//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Positive;

@Getter
public class OrderCreateRequestDto {

//    @NotNull(message = "레스토랑 ID는 필수입니다.")
    private Long restaurantId;

//    @NotNull(message = "메뉴 ID는 필수입니다.")
    private Long menuId;

//    @NotNull(message = "고객 ID는 필수입니다.")
    private Long userId;

//    @Positive(message = "총 금액은 0보다 커야 합니다.")
    private Integer totalPrice;

}