package com.sparta.spring26.domain.menu.repository;

import com.sparta.spring26.domain.menu.entity.Menu;
import com.sparta.spring26.domain.menu.entity.MenuStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByRestaurantIdAndStatusNot(Long restaurantId, MenuStatus status);
}
