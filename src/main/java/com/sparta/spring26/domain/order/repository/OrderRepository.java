package com.sparta.spring26.domain.order.repository;

import com.sparta.spring26.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByUserIdAndMenuId(Long useId, Long menuId);

    Optional<Order> findByIdAndUserId(Long orderId, Long userId);
}
