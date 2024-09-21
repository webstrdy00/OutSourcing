package com.sparta.spring26.domain.order.service;

import com.sparta.spring26.domain.order.OrderStatus;
import com.sparta.spring26.domain.order.dto.request.OrderCreateRequestDto;
import com.sparta.spring26.domain.order.dto.response.OrderResponseDto;
import com.sparta.spring26.domain.order.entity.Order;
import com.sparta.spring26.domain.order.event.OrderCreatedEvent;
import com.sparta.spring26.domain.order.repository.OrderRepository;
import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import com.sparta.spring26.domain.restaurant.repository.RestaurantRepository;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.global.exception.ErrorCode;
import com.sparta.spring26.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final RestaurantRepository restaurantRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public OrderResponseDto createOrder(User user, OrderCreateRequestDto orderCreateRequestDto){

        // 최소 주문 금액과 가게 오픈 시간 체크
        Restaurant restaurant = restaurantRepository.findById(orderCreateRequestDto.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.RESTAURANT_NOT_FOUND.getMessage()));

        if(orderCreateRequestDto.getTotalPrice() < restaurant.getMinDeliveryPrice()) {
            throw new IllegalArgumentException(
                    String.format("최소 주문 금액은 %d원입니다.", restaurant.getMinDeliveryPrice()));
        }

        // 가게 운영 시간 체크
        if (!isRestaurantOpen(restaurant.getOpenTime(), restaurant.getCloseTime())){
            throw new IllegalArgumentException(ErrorCode.MAX_RESTAURANT_LIMIT.getMessage());
        }

        // 초기 상태는 접수 중
        Order order = new Order();
        order.setRestaurant(restaurant);
        order.setTotalPrice(orderCreateRequestDto.getTotalPrice());
        order.setStatus(OrderStatus.ORDER_ACCEPTED);

        Order savedOrder = orderRepository.save(order);

        // 주문 생성 이벤트 발행
        eventPublisher.publishEvent(new OrderCreatedEvent(savedOrder));

        return new OrderResponseDto(
                savedOrder.getId(),
                orderCreateRequestDto.getMenuId(),
                restaurant.getId(),
                restaurant.getName(),
                savedOrder.getTotalPrice(),
                restaurant.getAddress(),
                savedOrder.getStatus()
        );
    }

    @Transactional
    public OrderResponseDto updateOrderStatus(User user, Long orderId, OrderStatus newStatus){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ORDER_NOT_FOUND.getMessage()));

        if (order.getMenu() == null){
            throw new IllegalArgumentException(ExceptionCode.MENU_NOT_FOUND.getMessage());
        }

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        return new OrderResponseDto(
                updatedOrder.getId(),
                updatedOrder.getMenu().getId(),
                updatedOrder.getRestaurant().getId(),
                updatedOrder.getRestaurant().getName(),
                updatedOrder.getTotalPrice(),
                updatedOrder.getRestaurant().getAddress(),
                updatedOrder.getStatus()
        );
    }

    // 주문 상세 조회
    public OrderResponseDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ORDER_NOT_FOUND.getMessage()));
        return new OrderResponseDto(
                order.getId(),
                order.getMenu().getId(),
                order.getRestaurant().getId(),
                order.getRestaurant().getName(),
                order.getTotalPrice(),
                order.getRestaurant().getAddress(),
                order.getStatus()
        );
    }

    // 모든 주문 리스트 조회
    public List<OrderResponseDto> getOrderList() {
        List<Order> orderList = orderRepository.findAll();
        return orderList.stream().map(order -> new OrderResponseDto(
                order.getId(),
                order.getMenu().getId(),
                order.getRestaurant().getId(),
                order.getRestaurant().getName(),
                order.getTotalPrice(),
                order.getRestaurant().getAddress(),
                order.getStatus()
        )).collect(Collectors.toList());
    }

    // 주문 삭제
    @Transactional
    public void deleteOrder(Long orderId, User user) {
        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ORDER_NOT_FOUND.getMessage()));

        // 주문 상태가 "주문 중"인지 확인
        if (!order.getStatus().equals(OrderStatus.ORDER_ACCEPTED)) {
            throw new IllegalArgumentException(ErrorCode.ORDER_NOT_CANCELLABLE.getMessage());
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.delete(order);
    }

    private boolean isRestaurantOpen(LocalTime openTime, LocalTime closeTime) {
        // 가게 운영 시간 체크 (가게마다 오픈/마감 시간 정보를 기반으로 확인)
        LocalTime currentTime = LocalTime.now();

        return currentTime.isAfter(openTime) && currentTime.isBefore(closeTime);
    }
}





