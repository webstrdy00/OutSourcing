package com.sparta.spring26.domain.order.controller;

import com.sparta.spring26.domain.order.dto.request.OrderCreateRequestDto;
import com.sparta.spring26.domain.order.dto.request.OrderStatusUpdateRequestDto;
import com.sparta.spring26.domain.order.dto.response.OrderResponseDto;
import com.sparta.spring26.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery/order")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderCreateRequestDto orderCreateRequestDto) {
        OrderResponseDto responseDto = orderService.createOrder(orderCreateRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestParam OrderStatusUpdateRequestDto statusUpdateRequestDto) {
        OrderResponseDto responseDto = orderService.updateOrderStatus(orderId, statusUpdateRequestDto.getNewStatus());
        return ResponseEntity.ok(responseDto);
    }
}
