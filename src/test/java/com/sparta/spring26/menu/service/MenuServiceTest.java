package com.sparta.spring26.menu.service;

import com.sparta.spring26.domain.menu.entity.Menu;
import com.sparta.spring26.domain.menu.entity.MenuStatus;
import com.sparta.spring26.domain.menu.repository.MenuRepository;
import com.sparta.spring26.domain.menu.service.MenuService;
import com.sparta.spring26.domain.restaurant.dto.request.RestaurantRequestDto;
import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import com.sparta.spring26.domain.restaurant.repository.RestaurantRepository;
import com.sparta.spring26.domain.user.dto.UserRequestDto;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.domain.user.entity.UserRole;
import com.sparta.spring26.global.exception.ExceptionCode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuService menuService;

    @Nested
    class CreateMenuTest {
        @Test
        void 메뉴_등록_성공() {
            // given
            User user = new User(new UserRequestDto(), "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);

            Long userId = 1L;

            ReflectionTestUtils.setField(user, "id", userId);

            Long restaurantId = 1L;
            String name = "menuName";
            String category = "main";
            Integer price = 50000;

            Restaurant restaurant = new Restaurant(new RestaurantRequestDto(), user);

            given(restaurantRepository.findById(anyLong())).willReturn(Optional.of(restaurant));

            // when
            menuService.createMenu(user, restaurantId, name, category, price);

            // then
            verify(menuRepository, times(1)).save(any(Menu.class));
        }

        @Test
        void 메뉴_등록중_가게가_존재하지_않는_예외() {
            // given
            User user = new User(new UserRequestDto(), "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);

            Long restaurantId = 1L;
            String name = "menuName";
            String category = "main";
            Integer price = 50000;

            given(restaurantRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> menuService.createMenu(user, restaurantId, name, category, price));

            // then
            assertEquals(ExceptionCode.RESTAURANT_NOT_FOUND.getMessage(), exception.getMessage());
        }

        @Test
        void 메뉴_등록중_가게에_대한_권한이_없는_예외() {
            // given
            User user1 = new User(new UserRequestDto(), "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);

            ReflectionTestUtils.setField(user1, "id", 1L);

            User user2 = new User(new UserRequestDto(), "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);

            ReflectionTestUtils.setField(user2, "id", 2L);


            Long restaurantId = 1L;
            String name = "menuName";
            String category = "main";
            Integer price = 50000;

            Restaurant restaurant = new Restaurant(new RestaurantRequestDto(), user2);

            given(restaurantRepository.findById(anyLong())).willReturn(Optional.of(restaurant));

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> menuService.createMenu(user1, restaurantId, name, category, price));

            // then
            assertEquals(ExceptionCode.RESTAURANT_OWNER_MISMATCH.getMessage(), exception.getMessage());

        }
    }

    @Nested
    class UpdateMenuTest {

        @Test
        void 메뉴_업데이트_성공() {
            // given
            User user = new User(new UserRequestDto(), "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);
            ReflectionTestUtils.setField(user, "id", 1L);

            Long restaurantId = 1L;
            Long menuId = 1L;
            String newName = "Updated Menu";
            String newCategory = "main";
            Integer newPrice = 60000;
            Boolean newPopularity = true;
            MenuStatus newStatus = MenuStatus.AVAILABLE;

            Restaurant restaurant = new Restaurant(new RestaurantRequestDto(), user);
            Menu menu = new Menu(restaurant, "Old Menu", 50000, "main");

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
            given(restaurantRepository.findById(anyLong())).willReturn(Optional.of(restaurant));

            // when
            menuService.updateMenu(user, restaurantId, menuId, newName, newCategory, newPrice, newPopularity, newStatus);

            // then
            assertEquals(newName, menu.getName());
            assertEquals(newCategory, menu.getCategory());
            assertEquals(newPrice, menu.getPrice());
            assertEquals(newPopularity, menu.getPopularity());
            assertEquals(newStatus, menu.getStatus());
        }

        @Test
        void 메뉴_업데이트중_메뉴가_존재하지_않는_예외() {
            // given
            User user = new User(new UserRequestDto(), "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);
            ReflectionTestUtils.setField(user, "id", 1L);
            Long restaurantId = 1L;
            Long menuId = 1L;
            String name = "menuName";
            String category = "main";
            Integer price = 50000;
            Boolean popularity = true;
            MenuStatus status = MenuStatus.AVAILABLE;

            given(menuRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> menuService.updateMenu(user, restaurantId, menuId, name, category, price, popularity, status));

            // then
            assertEquals(ExceptionCode.MENU_NOT_FOUND.getMessage(), exception.getMessage());
        }
    }
}
