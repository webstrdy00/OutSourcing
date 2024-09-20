package com.sparta.spring26.domain.order.aop;

import com.sparta.spring26.domain.order.entity.Order;
import com.sparta.spring26.global.entity.BaseTimeEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OrderAop extends BaseTimeEntity {

    @AfterReturning(pointcut = "execution(* com.example.OrderService.createOrder(..)) || execution(* com.example.OrderService.updateOrderStatus(..))", returning = "order")
    public void logOrder(JoinPoint joinPoint, Order order) {
        Long restaurantId = order.getId();
        Long OrderId = order.getId();
        BaseTimeEntity createdAt;
    }
}
