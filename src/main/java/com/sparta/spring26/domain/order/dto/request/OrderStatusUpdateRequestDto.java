package com.sparta.spring26.domain.order.dto.request;

import com.sparta.spring26.domain.order.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderStatusUpdateRequestDto {

    private OrderStatus newStatus;
}
