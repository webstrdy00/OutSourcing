package com.sparta.spring26.domain.menu.service;

import com.sparta.spring26.domain.menu.entity.Menu;
import com.sparta.spring26.domain.menu.entity.MenuStatus;
import com.sparta.spring26.domain.menu.repository.MenuRepository;
import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import com.sparta.spring26.domain.restaurant.repository.RestaurantRepository;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public void createMenu(User user, Long restaurantId, String name, String category, Integer price) {
        // restaurantId 검증
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new IllegalArgumentException(ExceptionCode.RESTAURANT_NOT_FOUND.getMessage()));

        // restaurant 의 user와 받아온 user 검증
        if(!restaurant.getOwner().getId().equals(user.getId())) {
            new IllegalArgumentException(ExceptionCode.RESTAURANT_OWNER_MISMATCH.getMessage());
        }

        // 등록
        menuRepository.save(new Menu(restaurant, name, price, category));
    }

    @Transactional
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
                new IllegalArgumentException(ExceptionCode.MENU_NOT_FOUND.getMessage()));

        // restaurantId 검증
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new IllegalArgumentException(ExceptionCode.RESTAURANT_NOT_FOUND.getMessage()));

        // restaurant의 menu인지 검증
        if(!menu.getRestaurant().equals(restaurant)) {
            throw new IllegalArgumentException(ExceptionCode.RESTAURANT_MENU_MISMATCH.getMessage());
        }

        // restaurant 의 user와 받아온 user 검증
        if(!restaurant.getOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException(ExceptionCode.RESTAURANT_OWNER_MISMATCH.getMessage());
        }

        menu.update(name, price, category, popularity, status);
    }


    public List<Menu> getMenus(Long restaurantId) {
        restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new IllegalArgumentException(ExceptionCode.RESTAURANT_NOT_FOUND.getMessage()));

        return menuRepository.findByRestaurantIdAndStatusNot(restaurantId, MenuStatus.DELETE);
    }

    @Transactional
    public void deleteMenu(User user, Long restaurantId, Long id) {
        // id값 검증
        Menu menu = menuRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(ExceptionCode.MENU_NOT_FOUND.getMessage()));

        // restaurantId 검증
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new IllegalArgumentException(ExceptionCode.RESTAURANT_NOT_FOUND.getMessage()));

        // restaurant의 menu인지 검증
        if(!menu.getRestaurant().equals(restaurant)) {
            throw new IllegalArgumentException(ExceptionCode.RESTAURANT_MENU_MISMATCH.getMessage());
        }

        // restaurant 의 user와 받아온 user 검증
        if(!restaurant.getOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException(ExceptionCode.RESTAURANT_OWNER_MISMATCH.getMessage());
        }

        menu.delete();
    }
}
