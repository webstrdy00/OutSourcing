package com.sparta.spring26.domain.restaurant.repository;

import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import com.sparta.spring26.domain.restaurant.enums.RestaurantCategory;
import com.sparta.spring26.domain.restaurant.enums.RestaurantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Page<Restaurant> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Restaurant> findAllByStatus(RestaurantStatus restaurantStatus, Pageable pageable);
    long countByOwnerIdAndStatus(Long ownerId, RestaurantStatus status);

    Page<Restaurant> findByNameContainingIgnoreCaseAndCategoryAndStatus(String name, RestaurantCategory category, RestaurantStatus restaurantStatus, Pageable pageable);

    Page<Restaurant> findByNameContainingIgnoreCaseAndStatus(String name, RestaurantStatus restaurantStatus, Pageable pageable);

    Page<Restaurant> findByCategoryAndStatus(RestaurantCategory category, RestaurantStatus restaurantStatus, Pageable pageable);
}
