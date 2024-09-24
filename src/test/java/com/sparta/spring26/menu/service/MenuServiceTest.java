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
import com.sparta.spring26.domain.user.enums.UserRole;
import com.sparta.spring26.global.exception.ExceptionCode;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
            assertEquals(newPopularity, menu.isPopularity());
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
            boolean popularity = true;
            MenuStatus status = MenuStatus.AVAILABLE;

            given(menuRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> menuService.updateMenu(user, restaurantId, menuId, name, category, price, popularity, status));

            // then
            assertEquals(ExceptionCode.MENU_NOT_FOUND.getMessage(), exception.getMessage());
        }

        @Test
        void 메뉴_업데이트중_가게가_존재하지_않는_예외() {
            // given
            User user = new User(new UserRequestDto(), "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);
            ReflectionTestUtils.setField(user, "id", 1L);

            Long restaurantId = 1L;
            Long menuId = 1L;
            String name = "menuName";
            String category = "main";
            Integer price = 50000;
            boolean popularity = true;
            MenuStatus status = MenuStatus.AVAILABLE;

            Menu menu = new Menu(new Restaurant(new RestaurantRequestDto(), user), name, price, category);

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
            given(restaurantRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> menuService.updateMenu(user, restaurantId, menuId, name, category, price, popularity, status));

            // then
            assertEquals(ExceptionCode.RESTAURANT_NOT_FOUND.getMessage(), exception.getMessage());
        }

        @Test
        void 메뉴_업데이트중_가게의_메뉴가_아닌_예외() {
            // given
            User user = new User(new UserRequestDto(), "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);
            ReflectionTestUtils.setField(user, "id", 1L);

            Long restaurantId = 1L;
            Long menuId = 1L;
            String name = "menuName";
            String category = "main";
            Integer price = 50000;
            boolean popularity = true;
            MenuStatus status = MenuStatus.AVAILABLE;

            Restaurant otherRestaurant = new Restaurant(new RestaurantRequestDto(), user);
            Menu menu = new Menu(otherRestaurant, name, price, category);

            Restaurant restaurant = new Restaurant(new RestaurantRequestDto(), user);

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
            given(restaurantRepository.findById(anyLong())).willReturn(Optional.of(restaurant));

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> menuService.updateMenu(user, restaurantId, menuId, name, category, price, popularity, status));

            // then
            assertEquals(ExceptionCode.RESTAURANT_MENU_MISMATCH.getMessage(), exception.getMessage());
        }

        @Test
        void 메뉴_업데이트중_권한이_없는_예외() {
            // given
            User owner = new User(new UserRequestDto(), "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);
            User otherUser = new User(new UserRequestDto(), "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);

            ReflectionTestUtils.setField(owner, "id", 1L);
            ReflectionTestUtils.setField(otherUser, "id", 2L);

            Long restaurantId = 1L;
            Long menuId = 1L;
            String name = "menuName";
            String category = "main";
            Integer price = 50000;
            boolean popularity = true;
            MenuStatus status = MenuStatus.AVAILABLE;

            Restaurant restaurant = new Restaurant(new RestaurantRequestDto(), owner);
            Menu menu = new Menu(restaurant, name, price, category);

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
            given(restaurantRepository.findById(anyLong())).willReturn(Optional.of(restaurant));

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> menuService.updateMenu(otherUser, restaurantId, menuId, name, category, price, popularity, status));

            // then
            assertEquals(ExceptionCode.RESTAURANT_OWNER_MISMATCH.getMessage(), exception.getMessage());
        }
    }

    @Nested
    class GetMenusTest {
        @Test
        void 메뉴_조회_성공() {
            // given
            Long restaurantId = 1L;

            User owner = new User(new UserRequestDto(), "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);
            Restaurant restaurant = new Restaurant(new RestaurantRequestDto(), owner);

            Menu menu1 = new Menu(restaurant, "Menu1", 15000, "main");
            Menu menu2 = new Menu(restaurant, "Menu2", 10000, "main");
            Menu menu3 = new Menu(restaurant, "Menu3", 8000, "side");
            Menu menu4 = new Menu(restaurant, "Menu4", 20000, "side");
            Menu menu5 = new Menu(restaurant, "Menu5", 25000, "drink");

            List<Menu> menus = Arrays.asList(menu1, menu2, menu3, menu4, menu5);

            given(restaurantRepository.findById(anyLong())).willReturn(Optional.of(restaurant));
            given(menuRepository.findByRestaurantIdAndStatusNot(anyLong(), any(MenuStatus.class))).willReturn(menus);

            // when
            List<Menu> result = menuService.getMenus(restaurantId);

            // then
            assertEquals(5, result.size());
            assertEquals("Menu1", result.get(0).getName());
            assertEquals("Menu2", result.get(1).getName());
            assertEquals("Menu3", result.get(2).getName());
            assertEquals("Menu4", result.get(3).getName());
            assertEquals("Menu5", result.get(4).getName());
        }

        @Test
        void 메뉴_조회중_가게가_존재하지_않는_예외() {
            // given
            Long restaurantId = 1L;

            given(restaurantRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> menuService.getMenus(restaurantId));

            // then
            assertEquals(ExceptionCode.RESTAURANT_NOT_FOUND.getMessage(), exception.getMessage());
        }
    }

    @Nested
    class DeleteMenuTest {
        @Test
        void 메뉴_삭제_성공() {
            // given
            Long menuId = 1L;
            Long restaurantId = 1L;

            User owner = new User(new UserRequestDto(), "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);
            ReflectionTestUtils.setField(owner, "id", 1L);

            Restaurant restaurant = new Restaurant(new RestaurantRequestDto(), owner);
            Menu menu = new Menu(restaurant, "menuName", 15000, "Main");

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
            given(restaurantRepository.findById(anyLong())).willReturn(Optional.of(restaurant));

            // when
            menuService.deleteMenu(owner, restaurantId, menuId);

            // then
            assertEquals(MenuStatus.DELETE, menu.getStatus());
        }

        @Test
        void 메뉴_삭제중_메뉴가_존재하지_않는_예외() {
            // given
            Long menuId = 1L;
            Long restaurantId = 1L;

            User owner = new User(new UserRequestDto(), "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);
            ReflectionTestUtils.setField(owner, "id", 1L);

            Restaurant restaurant = new Restaurant(new RestaurantRequestDto(), owner);
            Menu menu = new Menu(restaurant, "menuName", 15000, "Main");

            given(menuRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> menuService.deleteMenu(owner, restaurantId, menuId));

            // then
            assertEquals(ExceptionCode.MENU_NOT_FOUND.getMessage(), exception.getMessage());
        }

        @Test
        void 메뉴_삭제중_가게가_존재하지_않는_예외() {
            // given
            Long menuId = 1L;
            Long restaurantId = 1L;

            User owner = new User(new UserRequestDto(), "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);
            ReflectionTestUtils.setField(owner, "id", 1L);

            Restaurant restaurant = new Restaurant(new RestaurantRequestDto(), owner);
            Menu menu = new Menu(restaurant, "menuName", 15000, "Main");

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
            given(restaurantRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> menuService.deleteMenu(owner, restaurantId, menuId));

            // then
            assertEquals(ExceptionCode.RESTAURANT_NOT_FOUND.getMessage(), exception.getMessage());
        }

        @Test
        void 메뉴_삭제중_가게의_메뉴가_아닌_예외() {
            // given
            Long menuId = 1L;
            Long restaurantId = 1L;

            User owner = new User(new UserRequestDto(), "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);
            ReflectionTestUtils.setField(owner, "id", 1L);

            Restaurant restaurant1 = new Restaurant(new RestaurantRequestDto(), owner);
            Restaurant restaurant2 = new Restaurant(new RestaurantRequestDto(), owner);
            Menu menu = new Menu(restaurant1, "menuName", 15000, "Main");

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
            given(restaurantRepository.findById(anyLong())).willReturn(Optional.of(restaurant2));

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> menuService.deleteMenu(owner, restaurantId, menuId));

            // then
            assertEquals(ExceptionCode.RESTAURANT_MENU_MISMATCH.getMessage(), exception.getMessage());
        }

        @Test
        void 메뉴_삭제중_권한이_없는_예외() {
            // given
            Long menuId = 1L;
            Long restaurantId = 1L;

            User user = new User(new UserRequestDto(), "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);
            ReflectionTestUtils.setField(user, "id", 1L);

            User owner = new User(new UserRequestDto(), "$2a$10$Ywucr1lnT4w2XsdwfH9IiO8nOlOaIEFON6jRh1.E3wkhDfcX2j7eK", UserRole.OWNER);
            ReflectionTestUtils.setField(owner, "id", 2L);

            Restaurant restaurant = new Restaurant(new RestaurantRequestDto(), owner);
            Menu menu = new Menu(restaurant, "menuName", 15000, "Main");

            given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
            given(restaurantRepository.findById(anyLong())).willReturn(Optional.of(restaurant));

            // when
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> menuService.deleteMenu(user, restaurantId, menuId));

            // then
            assertEquals(ExceptionCode.RESTAURANT_OWNER_MISMATCH.getMessage(), exception.getMessage());
        }
    }
}
