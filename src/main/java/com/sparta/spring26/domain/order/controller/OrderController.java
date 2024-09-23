package com.sparta.spring26.domain.order.controller;

import com.sparta.spring26.domain.order.OrderStatus;
import com.sparta.spring26.domain.order.dto.request.OrderCreateRequestDto;
import com.sparta.spring26.domain.order.dto.request.OrderStatusUpdateRequestDto;
import com.sparta.spring26.domain.order.dto.response.OrderResponseDto;
import com.sparta.spring26.domain.order.service.OrderService;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery/order")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;

    /**
     * 주문 등록
     * @param userDetails 인증된 사용자 정보
     * @param orderCreateRequestDto 주문 생성 요청 데이터
     * @return 주문 응답 데이터, 상태 코드 201
     */
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails, // 인증된 사용자 정보
            @Valid @RequestBody OrderCreateRequestDto orderCreateRequestDto) {

        // 인증된 사용자 정보 가져오기
        User user = userDetails.getUser();

        // 권한 체크
        if (!userDetails.getAuthorities().stream()
                .noneMatch(authority -> authority.getAuthority().equals("USER"))) {
            throw new IllegalArgumentException("주문 등록은 일반 사용자만 가능합니다.");
        }
        // 주문 생성 서비스 호출
        OrderResponseDto responseDto = orderService.createOrder(user, orderCreateRequestDto);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * 주문 상태 업데이트
     * @param userDetails 인증된 사용자 정보
     * @param orderId 주문 ID
     * @param statusUpdateRequestDto 상태 업데이트 요청 데이터
     * @return 업데이트된 주문 응답 데이터, 상태 코드 200
     */
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderStatus> updateOrderStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequestDto statusUpdateRequestDto) {
        User user = userDetails.getUser();
        OrderStatus updateOrderStatus = orderService.updateOrderStatus(user, orderId, statusUpdateRequestDto.getNewStatus());
        return ResponseEntity.ok(updateOrderStatus);
    }

    /**
     * 주문 상세 조회
     * @param orderId 주문 ID
     * @return 주문 응답 데이터, 상태 코드 200
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
        OrderResponseDto orderResponseDto = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderResponseDto);
    }

    /**
     * 주문 삭제
     * @param orderId 주문 ID
     * @return 상태 코드 204 (No content)
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long orderId) {
        User user = userDetails.getUser();
        orderService.deleteOrder(orderId, user);
        return ResponseEntity.noContent().build();
    }

}
