package com.sparta.spring26.domain.order.service;

import com.sparta.spring26.domain.menu.entity.Menu;
import com.sparta.spring26.domain.menu.repository.MenuRepository;
import com.sparta.spring26.domain.order.OrderStatus;
import com.sparta.spring26.domain.order.dto.request.OrderCreateRequestDto;
import com.sparta.spring26.domain.order.dto.response.OrderResponseDto;
import com.sparta.spring26.domain.order.entity.Order;
import com.sparta.spring26.domain.order.repository.OrderRepository;
import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import com.sparta.spring26.domain.restaurant.repository.RestaurantRepository;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.global.exception.ErrorCode;
import com.sparta.spring26.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
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

    private final MenuRepository menuRepository;

    @Transactional
    public OrderResponseDto createOrder(User user, OrderCreateRequestDto orderCreateRequestDto) {

        // 가게 검증
        Restaurant restaurant = restaurantRepository.findById(orderCreateRequestDto.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.RESTAURANT_NOT_FOUND.getMessage()));

        // 메뉴 검증 및 설정
        Menu menu = menuRepository.findById(orderCreateRequestDto.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException(ExceptionCode.MENU_NOT_FOUND.getMessage()));

        // 총 가격 계산
        int totalPrice = menu.getPrice() * orderCreateRequestDto.getQuantity(); // 수량 반영

        // 최소 주문 금액
        if (totalPrice < restaurant.getMinDeliveryPrice()) {
            throw new IllegalArgumentException(
                    String.format("최소 주문 금액은 %d원입니다.", restaurant.getMinDeliveryPrice()));
        }

        // 가게 운영 시간 체크
        if (!isRestaurantOpen(restaurant.getOpenTime(), restaurant.getCloseTime())) {
            throw new IllegalArgumentException(ErrorCode.MAX_RESTAURANT_LIMIT.getMessage());
        }

        // 주문 설정
        Order order = new Order();
        order.setRestaurant(restaurant);
        order.setMenu(menu);
        order.setQuantity(orderCreateRequestDto.getQuantity());
        order.setTotalPrice(totalPrice);
        order.setStatus(OrderStatus.ORDER_ACCEPTED); // 초기 상태 설정
        order.setUser(user);

        Order savedOrder = orderRepository.save(order);

        return new OrderResponseDto(
                savedOrder.getId(),
                List.of(orderCreateRequestDto.getMenuId()),
                restaurant.getId(),
                restaurant.getName(),
                savedOrder.getTotalPrice(),
                restaurant.getAddress(),
                savedOrder.getStatus(),
                savedOrder.getCreatedAt(),
                savedOrder.getModifiedAt()
        );
    }

    @Transactional
    public OrderStatus updateOrderStatus(User user, Long orderId, OrderStatus newStatus){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionCode.ORDER_NOT_FOUND.getMessage()));

        order.setStatus(newStatus);
        orderRepository.save(order);

        return order.getStatus();
    }

    // 주문 상세 조회
    public OrderResponseDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionCode.ORDER_NOT_FOUND.getMessage()));

        return new OrderResponseDto(
                order.getId(),
                List.of(order.getMenu().getId()), // 단일 메뉴 ID
                order.getRestaurant().getId(),
                order.getRestaurant().getName(),
                order.getTotalPrice(),
                order.getRestaurant().getAddress(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getModifiedAt()
        );
    }


    // 주문 삭제
    @Transactional
    public void deleteOrder(Long orderId, User user) {
        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException(ExceptionCode.ORDER_NOT_FOUND.getMessage()));

        // 주문 상태가 "주문 중"인지 확인
        if (!order.getStatus().equals(OrderStatus.ORDER_ACCEPTED)) {
            throw new IllegalArgumentException(ExceptionCode.ORDER_NOT_CANCELLABLE.getMessage());
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





