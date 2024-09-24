package com.sparta.spring26.domain.restaurant.entity;

import com.sparta.spring26.domain.menu.entity.Menu;
import com.sparta.spring26.domain.order.entity.Order;
import com.sparta.spring26.domain.restaurant.dto.request.RestaurantRequestDto;
import com.sparta.spring26.domain.restaurant.dto.request.RestaurantUpdateDto;
import com.sparta.spring26.domain.restaurant.enums.RestaurantCategory;
import com.sparta.spring26.domain.restaurant.enums.RestaurantStatus;
import com.sparta.spring26.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer minDeliveryPrice;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private RestaurantStatus status;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private RestaurantCategory category;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> ordersList = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menuList = new ArrayList<>();

    public Restaurant(RestaurantRequestDto requestDto, User user) {
        this.name = requestDto.getName();
        this.description = requestDto.getDescription();
        this.minDeliveryPrice = requestDto.getMinDeliveryPrice();
        this.openTime = requestDto.getOpenTime();
        this.closeTime = requestDto.getCloseTime();
        this.address = requestDto.getAddress();
        this.owner = user;
        this.category = requestDto.getCategory();
        this.status = RestaurantStatus.OPEN;
    }

    public void updatePartial(RestaurantUpdateDto updateDto) {
        if (updateDto.getName() != null) {
            this.name = updateDto.getName();
        }
        if (updateDto.getDescription() != null) {
            this.description = updateDto.getDescription();
        }
        if (updateDto.getOpenTime() != null) {
            this.openTime = updateDto.getOpenTime();
        }
        if (updateDto.getCloseTime() != null) {
            this.closeTime = updateDto.getCloseTime();
        }
        if (updateDto.getMinDeliveryPrice() != null) {
            this.minDeliveryPrice = updateDto.getMinDeliveryPrice();
        }
        if (updateDto.getAddress() != null) {
            this.address = updateDto.getAddress();
        }
        if (updateDto.getCategory() != null) {
            this.category = updateDto.getCategory();
        }
    }

    public void close() {
        this.status = RestaurantStatus.CLOSED;
    }
}
