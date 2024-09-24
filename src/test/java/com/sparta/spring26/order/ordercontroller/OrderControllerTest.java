//package com.sparta.spring26.order.ordercontroller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sparta.spring26.domain.menu.entity.Menu;
//import com.sparta.spring26.domain.menu.repository.MenuRepository;
//import com.sparta.spring26.domain.order.OrderStatus;
//import com.sparta.spring26.domain.order.controller.OrderController;
//import com.sparta.spring26.domain.order.dto.request.OrderCreateRequestDto;
//import com.sparta.spring26.domain.order.entity.Order;
//import com.sparta.spring26.domain.order.repository.OrderRepository;
//import com.sparta.spring26.domain.restaurant.entity.Restaurant;
//import com.sparta.spring26.domain.restaurant.repository.RestaurantRepository;
//import com.sparta.spring26.domain.user.entity.User;
//import com.sparta.spring26.domain.user.entity.UserRole;
//import com.sparta.spring26.global.exception.ExceptionCode;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.time.LocalTime;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(OrderController.class)
//class OrderControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @InjectMocks
//    private OrderController orderController;
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private RestaurantRepository restaurantRepository;
//
//    @Mock
//    private MenuRepository menuRepository;
//
//    private User user;
//    private Restaurant restaurant;
//    private Menu menu;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // 사용자 정보 초기화
//        user = new User();
//        ReflectionTestUtils.setField(user, "id", 1L);
//        ReflectionTestUtils.setField(user, "role", UserRole.USER);
//
//        // 식당 정보 초기화
//        restaurant = new Restaurant();
//        ReflectionTestUtils.setField(restaurant, "id", 3L);
//        ReflectionTestUtils.setField(restaurant, "name", "Test Restaurant");
//        ReflectionTestUtils.setField(restaurant, "minDeliveryPrice", 10000);
//        ReflectionTestUtils.setField(restaurant, "openTime", LocalTime.of(9, 0));
//        ReflectionTestUtils.setField(restaurant, "closeTime", LocalTime.of(22, 0));
//
//        // 메뉴 정보 초기화
//        menu = new Menu();
//        ReflectionTestUtils.setField(menu, "id", 1L);
//        ReflectionTestUtils.setField(menu, "price", 12000);
//        ReflectionTestUtils.setField(menu, "restaurant", restaurant);
//    }
//
//    @Test
//    void 주문_등록_성공() throws Exception {
//        // Given
//        OrderCreateRequestDto requestDto = new OrderCreateRequestDto(3L, 1L, 1); // restaurantId는 3L
//
//        when(restaurantRepository.findById(3L)).thenReturn(Optional.of(restaurant));
//        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
//
//        Order order = new Order();
//        ReflectionTestUtils.setField(order, "id", 1L);
//        ReflectionTestUtils.setField(order, "user", user);
//        ReflectionTestUtils.setField(order, "restaurant", restaurant);
//        ReflectionTestUtils.setField(order, "menu", menu);
//        ReflectionTestUtils.setField(order, "quantity", 1);
//        ReflectionTestUtils.setField(order, "totalPrice", menu.getPrice());
//        ReflectionTestUtils.setField(order, "status", OrderStatus.ORDER_ACCEPTED);
//
//        when(orderRepository.save(any(Order.class))).thenReturn(order);
//
//        // When & Then
//        mockMvc.perform(post("/delivery/order")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(requestDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.restaurantId").value(3L))
//                .andExpect(jsonPath("$.totalPrice").value(12000))
//                .andExpect(jsonPath("$.status").value(OrderStatus.ORDER_ACCEPTED.name()));
//    }
//
//    @Test
//    void 주문_등록_실패_메뉴_미존재() throws Exception {
//        // Given
//        when(restaurantRepository.findById(3L)).thenReturn(Optional.of(restaurant));
//        when(menuRepository.findById(1L)).thenReturn(Optional.empty());
//
//        OrderCreateRequestDto requestDto = new OrderCreateRequestDto(3L, 1L, 1);
//
//        // When & Then
//        mockMvc.perform(post("/delivery/order")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(requestDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value(ExceptionCode.MENU_NOT_FOUND.getMessage()));
//    }
//}