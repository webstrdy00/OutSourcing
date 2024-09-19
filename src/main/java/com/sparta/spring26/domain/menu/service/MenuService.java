package com.sparta.spring26.domain.menu.service;

import com.sparta.spring26.domain.menu.entity.Menu;
import com.sparta.spring26.domain.menu.entity.MenuStatus;
import com.sparta.spring26.domain.menu.repository.MenuRepository;
import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import com.sparta.spring26.domain.restaurant.repository.RestaurantRepository;
import com.sparta.spring26.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public void createMenu(User user, Long restaurantId, String name, String category, Integer price) {
        // restaurantId 검증
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new IllegalArgumentException("Restaurant not found"));

        // restaurant 의 user와 받아온 user 검증
        if(!restaurant.getOwner().equals(user)) {
            new IllegalArgumentException("Restaurant owner is not the owner of the menu");
        }

        // 등록
        menuRepository.save(new Menu(restaurant, name, price, category));
    }

    public void updateMenu(
            User user,
            Long restaurantId,
            Long id,
            String name,
            String category,
            Integer price,
            Boolean popularity,
            MenuStatus status) {
        // id값 검증
        Menu menu = menuRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Menu not found"));

        // restaurantId 검증
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new IllegalArgumentException("Restaurant not found"));

        // restaurant 의 user와 받아온 user 검증
        if(!restaurant.getOwner().equals(user)) {
            new IllegalArgumentException("Restaurant owner is not the owner of the menu");
        }

        menu.update(name, price, category, popularity, status);
    }


}
