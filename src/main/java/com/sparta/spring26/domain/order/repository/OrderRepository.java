package com.sparta.spring26.domain.order.repository;

import com.sparta.spring26.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
