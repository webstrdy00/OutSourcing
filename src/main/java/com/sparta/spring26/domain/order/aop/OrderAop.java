package com.sparta.spring26.domain.order.aop;

import com.sparta.spring26.domain.order.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class OrderAop {

    private static final Logger logger = LoggerFactory.getLogger(OrderAop.class);

    @AfterReturning(pointcut = "execution(* com.sparta.spring26.domain.order.service.OrderService.createOrder(..))", returning = "result")
    public void logOrderServie(Object result) {
        if (result instanceof Order) {
            Order order = (Order) result;
            Long orderId = order.getId();
            Long restaurantId = order.getRestaurant().getId();
            logger.info("주문 처리됨. Order ID {}, Restaurant ID: {}", orderId, restaurantId);
        }
    }
}
