package com.sparta.spring26.domain.order.dto.response;

import com.sparta.spring26.domain.order.OrderStatus;
import com.sparta.spring26.global.entity.BaseTimeEntity;
import lombok.Getter;

@Getter
public class OrderResponseDto extends BaseTimeEntity {

    private Long orderId;
    private Long menuId;
    private Long restaurantId;
    private String restaurantName;
    private Integer totalAmount;
    private String address;
    private OrderStatus status;

    public OrderResponseDto(Long orderId, Long menuId, Long restaurantId, String restaurantName,
                            Integer totalAmount, String address, OrderStatus status) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.totalAmount = totalAmount;
        this.address = address;
        this.status = status;
    }
}