package com.sparta.spring26.domain.order.controller;

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
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("USER"))){
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
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequestDto statusUpdateRequestDto) {
        User user = userDetails.getUser();
        OrderResponseDto responseDto = orderService.updateOrderStatus(user, orderId, statusUpdateRequestDto.getNewStatus());
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 주문 상세 조회
     * @param orderId 주문 ID
     * @return 주문 응답 데이터, 상태 코드 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
        OrderResponseDto orderResponseDto = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderResponseDto);
    }

    /**
     * 모든 주문 리스트 조회
     * @return 주문 응답 데이터 리스트, 상태 코드 200
     */
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrderList() {
        List<OrderResponseDto> orderList = orderService.getOrderList();
        return ResponseEntity.ok(orderList);
    }

    /**
     * 주문 삭제
     * @param orderId 주문 ID
     * @return 상태 코드 204 (No content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long orderId) {
        User user = userDetails.getUser();
        orderService.deleteOrder(orderId, user);
        return ResponseEntity.noContent().build();
    }

}
