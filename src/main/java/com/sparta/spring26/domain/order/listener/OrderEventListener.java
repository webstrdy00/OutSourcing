package com.sparta.spring26.domain.order.listener;

import com.sparta.spring26.domain.order.entity.Order;
import com.sparta.spring26.domain.order.event.OrderCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

        @EventListener
        public void handleOrderCreated(OrderCreatedEvent event) {
            Order order = event.getOrder();
            // 주문 생성 후 필요한 작업을 수행
            System.out.println("주문이 생성되었습니다: " + order.getId());
        }
    }
