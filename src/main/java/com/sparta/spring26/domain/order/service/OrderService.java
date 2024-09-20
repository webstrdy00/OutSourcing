package com.sparta.spring26.domain.order.service;

import com.sparta.spring26.domain.order.OrderStatus;
import com.sparta.spring26.domain.order.dto.request.OrderCreateRequestDto;
import com.sparta.spring26.domain.order.dto.response.OrderResponseDto;
import com.sparta.spring26.domain.order.entity.Order;
import com.sparta.spring26.domain.order.repository.OrderRepository;
import com.sparta.spring26.domain.restaurant.RestaurantRepository;
import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final RestaurantRepository restaurantRepository;

    public OrderResponseDto createOrder(OrderCreateRequestDto orderCreateRequestDto){

        // 최소 주문 금액과 가게 오픈 시간 체크
        Restaurant restaurant = restaurantRepository.findById(orderCreateRequestDto.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("해당 레스토랑을 찾을 수 없습니다."));

        if(orderCreateRequestDto.getTotalPrice() < restaurant.getMinDeliveryPrice()) {
            throw new IllegalArgumentException(
                    String.format("최소 주문 금액은 %d원입니다.", restaurant.getMinDeliveryPrice()));
        }

        if (!isRestaurantOpen(restaurant.getOperationHours())){
            throw new IllegalArgumentException("가게 운영 시간이 아닙니다.");
        }

        // 초기 상태는 접수 중
        Order order = new Order();
        order.setRestaurant(restaurant);
        order.setTotalPrice(orderCreateRequestDto.getTotalPrice());
        order.setStatus(OrderStatus.ORDER_ACCEPTED);

        Order savedOrder = orderRepository.save(order);

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

    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus newStatus){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        if (order.getMenu() == null){
            throw new IllegalArgumentException("주문에 메뉴 정보가 없습니다.");
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

    private boolean isRestaurantOpen(String operationHours) {
        // 가게 운영 시간 체크 (가게마다 오픈/마감 시간 정보를 기반으로 확인)
        String[] hours = operationHours.split("-");
        LocalTime openTime = LocalTime.parse(hours[0], DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime closeTime = LocalTime.parse(hours[1], DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime currentTime = LocalTime.now();

        return currentTime.isAfter(openTime) && currentTime.isBefore(closeTime);
    }
}





