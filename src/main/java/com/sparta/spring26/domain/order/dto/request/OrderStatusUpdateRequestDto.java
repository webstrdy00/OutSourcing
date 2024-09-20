package com.sparta.spring26.domain.order.dto.request;

import com.sparta.spring26.domain.order.OrderStatus;
import lombok.Getter;

@Getter
public class OrderStatusUpdateRequestDto {

    private OrderStatus newStatus;
}
