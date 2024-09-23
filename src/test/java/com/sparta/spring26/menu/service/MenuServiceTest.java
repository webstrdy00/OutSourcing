package com.sparta.spring26.menu.service;

import com.sparta.spring26.domain.menu.entity.Menu;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
}
