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
import com.sparta.spring26.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
            UserRequestDto userRequestDto = new UserRequestDto();

            User user = new User(userRequestDto, "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);

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
    }
}
