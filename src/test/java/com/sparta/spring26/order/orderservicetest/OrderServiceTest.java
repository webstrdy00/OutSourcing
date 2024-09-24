package com.sparta.spring26.order.orderservicetest;

import com.sparta.spring26.domain.menu.entity.Menu;
import com.sparta.spring26.domain.menu.repository.MenuRepository;
import com.sparta.spring26.domain.order.OrderStatus;
import com.sparta.spring26.domain.order.dto.request.OrderCreateRequestDto;
import com.sparta.spring26.domain.order.dto.response.OrderResponseDto;
import com.sparta.spring26.domain.order.entity.Order;
import com.sparta.spring26.domain.order.repository.OrderRepository;
import com.sparta.spring26.domain.order.service.OrderService;
import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import com.sparta.spring26.domain.restaurant.repository.RestaurantRepository;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.domain.user.entity.UserRole;
import com.sparta.spring26.global.exception.ExceptionCode;
import com.sparta.spring26.global.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private UserDetailsImpl userDetails;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuRepository menuRepository;

    private User user;
    private Restaurant restaurant;
    private Menu menu;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 테스트를 위한 User, Restaurant, Menu 객체 초기화
        user = new User();
        ReflectionTestUtils.setField(user, "id", 1L); // 예시로 ID 설정

        restaurant = new Restaurant();
        ReflectionTestUtils.setField(restaurant, "id", 1L);
        ReflectionTestUtils.setField(restaurant, "name", "Test Restaurant");
        ReflectionTestUtils.setField(restaurant, "minDeliveryPrice", 10000);
        ReflectionTestUtils.setField(restaurant, "openTime", LocalTime.of(9, 0));
        ReflectionTestUtils.setField(restaurant, "closeTime", LocalTime.of(21, 0));
        ReflectionTestUtils.setField(restaurant, "address", "123 Test Street");

        menu = new Menu();
        ReflectionTestUtils.setField(menu, "id", 1L);
        ReflectionTestUtils.setField(menu, "price", 12000);
        ReflectionTestUtils.setField(menu, "restaurant", restaurant);
    }

    @Test
    void 주문_등록_성공() {
        // Given
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(user, "role", UserRole.USER);

        // 식당 정보 생성
        LocalTime openTime = LocalTime.of(9, 0);  // 9:00 AM
        LocalTime closeTime = LocalTime.of(22, 0); // 10:00 PM
        Restaurant restaurant = new Restaurant();
        ReflectionTestUtils.setField(restaurant, "id", 3L);
        ReflectionTestUtils.setField(restaurant, "name", "Test Restaurant");
        ReflectionTestUtils.setField(restaurant, "minDeliveryPrice", 10000);
        ReflectionTestUtils.setField(restaurant, "openTime", openTime);
        ReflectionTestUtils.setField(restaurant, "closeTime", closeTime);

        when(restaurantRepository.findById(3L)).thenReturn(Optional.of(restaurant));

        Menu menu = new Menu();
        ReflectionTestUtils.setField(menu, "id", 1L);
        ReflectionTestUtils.setField(menu, "price", 12000);
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        // 주문 시간 설정(운영시간 내)
        LocalDateTime orderTime = LocalDateTime.of(2024, 9, 24, 10, 0); // 오전 10시
        OrderCreateRequestDto requestDto = new OrderCreateRequestDto(3L, 1L, 1); // restaurantId는 3L


        // Mocking the order saving
        Order order = new Order();
        ReflectionTestUtils.setField(order, "id", 1L); // 가상의 ID 설정
        ReflectionTestUtils.setField(order, "user", user);
        ReflectionTestUtils.setField(order, "restaurant", restaurant);
        ReflectionTestUtils.setField(order, "menu", menu);
        ReflectionTestUtils.setField(order, "quantity", 1);
        ReflectionTestUtils.setField(order, "totalPrice", menu.getPrice() * 1);
        ReflectionTestUtils.setField(order, "status", OrderStatus.ORDER_ACCEPTED);


        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        OrderResponseDto responseDto = orderService.createOrder(user, requestDto);

        // Then
        assertEquals(1L, responseDto.getId()); // 저장된 주문 ID
        assertEquals(3L, responseDto.getRestaurantId());
        assertEquals(12000, responseDto.getTotalPrice());
        assertEquals(OrderStatus.ORDER_ACCEPTED, responseDto.getStatus());
        assertNotNull(responseDto);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void 주문_등록_실패_메뉴_미존재() {
        // Given
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuRepository.findById(1L)).thenReturn(Optional.empty());

        OrderCreateRequestDto requestDto = new OrderCreateRequestDto();
        ReflectionTestUtils.setField(requestDto, "restaurantId", 1L);
        ReflectionTestUtils.setField(requestDto, "menuId", 1L);
        ReflectionTestUtils.setField(requestDto, "quantity", 1);

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(user, requestDto);
        });

        assertEquals(ExceptionCode.MENU_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 주문_상태_업데이트_성공() {
        // Given
        Order order = new Order();
        ReflectionTestUtils.setField(order, "id", 1L);
        ReflectionTestUtils.setField(order, "status", OrderStatus.ORDER_ACCEPTED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // When
        OrderStatus updatedStatus = orderService.updateOrderStatus(user, 1L, OrderStatus.ORDER_RECEIVED);

        // Then
        assertEquals(OrderStatus.ORDER_RECEIVED, updatedStatus);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void 주문_상태_업데이트_실패_주문_미존재() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrderStatus(user, 1L, OrderStatus.ORDER_RECEIVED);
        });

        assertEquals(ExceptionCode.ORDER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 주문_상세_조회_성공() {
        // Given
        Order order = new Order();
        ReflectionTestUtils.setField(order, "id", 1L);
        ReflectionTestUtils.setField(order, "menu", menu);
        ReflectionTestUtils.setField(order, "restaurant", restaurant);
        ReflectionTestUtils.setField(order, "totalPrice", 12000);
        ReflectionTestUtils.setField(order, "status", OrderStatus.ORDER_ACCEPTED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // When
        OrderResponseDto responseDto = orderService.getOrder(1L);

        // Then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals(12000, responseDto.getTotalPrice());
        assertEquals(OrderStatus.ORDER_ACCEPTED, responseDto.getStatus());
    }

    @Test
    void 주문_삭제_성공() {
        // Given
        Order order = new Order();
        ReflectionTestUtils.setField(order, "id", 1L);
        ReflectionTestUtils.setField(order, "user", user);
        ReflectionTestUtils.setField(order, "status", OrderStatus.ORDER_ACCEPTED);
        when(orderRepository.findByIdAndUserId(1L, user.getId())).thenReturn(Optional.of(order));

        // When
        orderService.deleteOrder(1L, user);

        // Then
        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void 주문_삭제_실패_주문_미존재() {
        // Given
        when(orderRepository.findByIdAndUserId(1L, user.getId())).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.deleteOrder(1L, user);
        });

        assertEquals(ExceptionCode.ORDER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 주문_삭제_실패_주문_취소불가() {
        // Given
        Order order = new Order();
        ReflectionTestUtils.setField(order, "id", 1L);
        ReflectionTestUtils.setField(order, "user", user);
        ReflectionTestUtils.setField(order, "status", OrderStatus.ORDER_RECEIVED); // 주문 상태가 변경되어 취소 불가
        when(orderRepository.findByIdAndUserId(1L, user.getId())).thenReturn(Optional.of(order));

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.deleteOrder(1L, user);
        });

        assertEquals(ExceptionCode.ORDER_NOT_CANCELLABLE.getMessage(), exception.getMessage());
    }
}