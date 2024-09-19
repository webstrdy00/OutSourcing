package com.sparta.spring26.domain.restaurant.repository;

import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
