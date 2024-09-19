package com.sparta.spring26.domain.menu.entity;

import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import com.sparta.spring26.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "menus")
public class Menu extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false)
    private Boolean popularity;

    @Column(nullable = false, length = 100)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}
