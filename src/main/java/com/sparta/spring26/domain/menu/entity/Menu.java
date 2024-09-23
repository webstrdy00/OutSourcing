package com.sparta.spring26.domain.menu.entity;

import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import com.sparta.spring26.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "menus")
@NoArgsConstructor
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

    @Column
    private Boolean popularity;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MenuStatus status = MenuStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Menu(Restaurant restaurant, String name, Integer price, String category) {
        this.restaurant = restaurant;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public void update(String name, Integer price, String category, boolean popularity, MenuStatus status) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.popularity = popularity;
        this.status = status;
    }

    public void delete() {
        this.status = MenuStatus.DELETE;
    }
}
