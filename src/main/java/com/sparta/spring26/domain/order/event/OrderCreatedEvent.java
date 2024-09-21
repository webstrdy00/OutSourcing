package com.sparta.spring26.domain.order.event;

import com.sparta.spring26.domain.order.entity.Order;
import lombok.Getter;

@Getter
public class OrderCreatedEvent{
    private final Order order;

    public OrderCreatedEvent(Order order){
        this.order = order;
    }
}
