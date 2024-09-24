package com.sparta.spring26.order.ordercontroller;

import com.sparta.spring26.domain.order.OrderStatus;
import com.sparta.spring26.domain.order.controller.OrderController;
import com.sparta.spring26.domain.order.dto.request.OrderCreateRequestDto;
import com.sparta.spring26.domain.order.dto.request.OrderStatusUpdateRequestDto;
import com.sparta.spring26.domain.order.dto.response.OrderResponseDto;
import com.sparta.spring26.domain.order.service.OrderService;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.global.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private UserDetailsImpl userDetails;

    private User user;
    private OrderCreateRequestDto orderCreateRequestDto;
    private OrderResponseDto orderResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        // 필요한 사용자 정보 설정 (예: ID, 이름 등)
        orderCreateRequestDto = new OrderCreateRequestDto();
        // 요청 DTO 설정
        orderResponseDto = new OrderResponseDto();
        // 응답 DTO 설정
    }

//    @Test
//    void 주문_등록_성공() {
//        // Given
//        when(userDetails.getUser()).thenReturn(user);
//        when(userDetails.getAuthorities()).thenReturn(List.of(new SimpleGrantedAuthority("USER")));
//        when(orderService.createOrder(any(User.class), any(OrderCreateRequestDto.class)))
//                .thenReturn(orderResponseDto);
//
//        // When
//        ResponseEntity<OrderResponseDto> responseEntity = orderController.createOrder(userDetails, orderCreateRequestDto);
//
//        // Then
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(orderResponseDto, responseEntity.getBody());
//        verify(orderService, times(1)).createOrder(user, orderCreateRequestDto);
//    }

//    @Test
//    void 주문_등록_실패_권한_체크() {
//        // Given
//        when(userDetails.getUser()).thenReturn(user);
//        when(userDetails.getAuthorities()).thenReturn(List.of(new SimpleGrantedAuthority("ADMIN"))); // 권한이 USER가 아님
//
//        // When & Then
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            orderController.createOrder(userDetails, orderCreateRequestDto);
//        });
//
//        assertEquals("주문 등록은 일반 사용자만 가능합니다.", exception.getMessage());
//    }

    @Test
    void 주문_상태_업데이트_성공() {
        // Given
        Long orderId = 1L;
        OrderStatusUpdateRequestDto statusUpdateRequestDto = new OrderStatusUpdateRequestDto();
        // 상태 업데이트 DTO 설정
        OrderStatus updatedStatus = OrderStatus.ORDER_RECEIVED;
        when(userDetails.getUser()).thenReturn(user);
        when(orderService.updateOrderStatus(any(User.class), eq(orderId), any(OrderStatus.class)))
                .thenReturn(updatedStatus);

        // When
        ResponseEntity<OrderStatus> responseEntity = orderController.updateOrderStatus(userDetails, orderId, statusUpdateRequestDto);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedStatus, responseEntity.getBody());
        verify(orderService, times(1)).updateOrderStatus(user, orderId, statusUpdateRequestDto.getNewStatus());
    }

    @Test
    void 주문_상세_조회_성공() {
        // Given
        Long orderId = 1L;
        when(orderService.getOrder(orderId)).thenReturn(orderResponseDto);

        // When
        ResponseEntity<OrderResponseDto> responseEntity = orderController.getOrder(orderId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(orderResponseDto, responseEntity.getBody());
        verify(orderService, times(1)).getOrder(orderId);
    }

    @Test
    void 주문_삭제_성공() {
        // Given
        Long orderId = 1L;
        when(userDetails.getUser()).thenReturn(user);

        // When
        ResponseEntity<Void> responseEntity = orderController.deleteOrder(userDetails, orderId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(orderService, times(1)).deleteOrder(orderId, user);
    }
}