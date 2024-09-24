//package com.sparta.spring26.orderservicetest;
//
//import com.sparta.spring26.domain.menu.entity.Menu;
//import com.sparta.spring26.domain.order.OrderStatus;
//import com.sparta.spring26.domain.order.dto.request.OrderCreateRequestDto;
//import com.sparta.spring26.domain.order.dto.response.OrderResponseDto;
//import com.sparta.spring26.domain.order.entity.Order;
//import com.sparta.spring26.domain.order.repository.OrderRepository;
//import com.sparta.spring26.domain.order.service.OrderService;
//import com.sparta.spring26.domain.restaurant.entity.Restaurant;
//import com.sparta.spring26.domain.restaurant.repository.RestaurantRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//public class OrderServiceTest {
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private RestaurantRepository restaurantRepository;
//
//    @InjectMocks
//    private OrderService orderService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void 주문_등록_성공() {
//
//        // Given
//        OrderCreateRequestDto requestDto = new OrderCreateRequestDto();
//        ReflectionTestUtils.setField(requestDto, "restaurantId", 1L);
//        ReflectionTestUtils.setField(requestDto, "totalPrice", 12000);
//
//        Restaurant restaurant = new Restaurant();
//        ReflectionTestUtils.setField(restaurant, "id", 1L);
//        ReflectionTestUtils.setField(restaurant, "name","Test Restaurant");
//        ReflectionTestUtils.setField(restaurant, "minDeliveryPrice", 10000);
//        ReflectionTestUtils.setField(restaurant, "operationHours", "09:00-22:00");
//        ReflectionTestUtils.setField(restaurant, "address", "123 Test street");
//
//        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
//
//        com.sparta.spring26.domain.order.entity.Order order = new com.sparta.spring26.domain.order.entity.Order();
//        ReflectionTestUtils.setField(order, "id", 1L);
//        ReflectionTestUtils.setField(order, "totalPrice", requestDto.getTotalPrice());
//        ReflectionTestUtils.setField(order, "restaurant", restaurant);
//        ReflectionTestUtils.setField(order, "status", OrderStatus.ORDER_ACCEPTED);
//
//        when(orderRepository.save(any(com.sparta.spring26.domain.order.entity.Order.class))).thenReturn(order);
//
//        // When
//        OrderResponseDto response = orderService.createOrder(requestDto);
//
//        // Then
//        assertNotNull(response);
//        assertEquals(1L, response.getRestaurantId());
//        assertEquals("Test Restaurant", response.getRestaurantName());
//        assertEquals(12000, response.getTotalAmount());
//        verify(orderRepository, times(1)).save(any(Order.class));
//
//    }
//
//    @Test
//    void 주문_최소_금액_충족_실패() {
//
//        // Given
//        OrderCreateRequestDto requestDto = new OrderCreateRequestDto();
//        ReflectionTestUtils.setField(requestDto, "restaurantId", 1L);
//        ReflectionTestUtils.setField(requestDto, "totalPrice", 8000);
//
//        Restaurant restaurant = new Restaurant();
//        ReflectionTestUtils.setField(restaurant, "id", 1L);
//        ReflectionTestUtils.setField(restaurant, "minDeliveryPrice", 10000);
//        ReflectionTestUtils.setField(restaurant, "operationHours", "09:00-22:00");
//        ReflectionTestUtils.setField(restaurant, "address", "123 Test Street");
//
//        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
//
//        // When & Then
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            orderService.createOrder(requestDto);
//        });
//
//        assertEquals("최소 주문 금액은 10000원입니다.", exception.getMessage());
//        verify(orderRepository, times(0)).save(any(Order.class));
//    }
//
//    @Test
//    void 주문_상태_업데이트_성공() {
//
//        // Given
//        Menu menu = new Menu();
//        ReflectionTestUtils.setField(menu, "id", 1L);
//        ReflectionTestUtils.setField(menu, "name", "치킨");
//        ReflectionTestUtils.setField(menu, "price", 15000);
//        ReflectionTestUtils.setField(menu, "category", "메인");
//        ReflectionTestUtils.setField(menu, "popularity", true);
//        ReflectionTestUtils.setField(menu, "status", "판매중");
//
//        Restaurant restaurant = new Restaurant();
//        ReflectionTestUtils.setField(restaurant, "id", 1L);
//        ReflectionTestUtils.setField(restaurant, "name", "맛있는 치킨집");
//        ReflectionTestUtils.setField(restaurant, "minDeliveryPrice", 10000);
//        ReflectionTestUtils.setField(restaurant, "operationHours", "09:00-22:00");
//        ReflectionTestUtils.setField(restaurant, "address", "123 Test Street");
//
//        Order order = new Order();
//        ReflectionTestUtils.setField(order, "id", 1L);
//        ReflectionTestUtils.setField(order, "totalPrice", 12000);
//        ReflectionTestUtils.setField(order, "status", OrderStatus.ORDER_RECEIVED);
//        ReflectionTestUtils.setField(order, "menu", menu); // 메뉴 설정
//        ReflectionTestUtils.setField(order, "restaurant", restaurant); // 레스토랑 설정
//
//        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
//
//        Order updatedOrder = new Order();
//        ReflectionTestUtils.setField(updatedOrder, "id", 1L);
//        ReflectionTestUtils.setField(updatedOrder, "status", OrderStatus.ORDER_ACCEPTED);
//        ReflectionTestUtils.setField(updatedOrder, "menu", menu); // 메뉴 설정
//        ReflectionTestUtils.setField(updatedOrder, "restaurant", restaurant); // 레스토랑 설정
//
//        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);
//
//        // When
//        OrderResponseDto response = orderService.updateOrderStatus(1L, OrderStatus.ORDER_ACCEPTED);
//
//        // Then
//        assertNotNull(response);
//        assertEquals(OrderStatus.ORDER_ACCEPTED, response.getStatus());
//        verify(orderRepository, times(1)).save(any(Order.class));
//    }
//}
