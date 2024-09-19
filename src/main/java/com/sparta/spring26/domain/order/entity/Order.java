package com.sparta.spring26.domain.order.entity;

import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}
