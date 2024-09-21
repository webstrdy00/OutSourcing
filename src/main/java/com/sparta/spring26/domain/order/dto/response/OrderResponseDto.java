package com.sparta.spring26.domain.order.dto.response;

import com.sparta.spring26.domain.order.OrderStatus;
import com.sparta.spring26.global.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderResponseDto extends BaseTimeEntity {

    private Long id;
    private Long menuId;
    private Long restaurantId;
    private String restaurantName;
    private Integer totalPrice;
    private String address;
    private OrderStatus status;

    public OrderResponseDto(Long id, Long menuId, Long restaurantId, String restaurantName,
                            Integer totalPrice, String address, OrderStatus status) {
        this.id = id;
        this.menuId = menuId;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.totalPrice = totalPrice;
        this.address = address;
        this.status = status;
    }
}