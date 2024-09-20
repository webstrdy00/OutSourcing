package com.sparta.spring26.domain.order.controller;

import com.sparta.spring26.domain.order.dto.request.OrderCreateRequestDto;
import com.sparta.spring26.domain.order.dto.request.OrderStatusUpdateRequestDto;
import com.sparta.spring26.domain.order.dto.response.OrderResponseDto;
import com.sparta.spring26.domain.order.service.OrderService;
import com.sparta.spring26.domain.restaurant.RestaurantRepository;
import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public RequestEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderCreateRequestDto orderCreateRequestDto) {
        OrderResponseDto responseDto = orderService.createOrder(orderCreateRequestDto);
        return new RequestEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}/status")
    public RequestEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestParam OrderStatusUpdateRequestDto statusUpdateRequestDto) {
        OrderResponseDto responseDto = orderService.updateOrderStatus(orderId, statusUpdateRequestDto.getNewStatus());
        return new RequestEntity<>(responseDto, HttpStatus.OK);
    }
}
