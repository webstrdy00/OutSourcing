package com.sparta.spring26.domain.cart.service;

import com.sparta.spring26.domain.cart.dto.request.CartRequestDto;
import com.sparta.spring26.domain.cart.dto.response.CartResponseDto;
import com.sparta.spring26.domain.cart.entity.Cart;
import com.sparta.spring26.domain.cart.repository.CartRepository;
import com.sparta.spring26.domain.menu.entity.Menu;
import com.sparta.spring26.domain.menu.repository.MenuRepository;
import com.sparta.spring26.domain.restaurant.repository.RestaurantRepository;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.domain.user.repository.UserRepository;
import com.sparta.spring26.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public CartResponseDto addItem(CartRequestDto requestDto, User user) {

        // 메뉴 조회
        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException(ExceptionCode.MENU_NOT_FOUND.getMessage()));

        // 유저의 장바구니 조회 또는 새로 생성
        Cart cart = cartRepository.findByUser(user.getId())
                .orElseGet(() -> createNewCart(user));

        // 다른 가게 메뉴가 추가될 경우 초기화
        if (cart.getRestaurant() != null && !cart.getRestaurant().equals(menu.getRestaurant())) {
            cart.setRestaurant(menu.getRestaurant());
            cart.getItems().clear();
        } else if (cart.getRestaurant() == null) {
            cart.setRestaurant(menu.getRestaurant());
        }

        // 메뉴 추가
        cart.addItem(menu, requestDto.getQuantity());

        // 장바구니 저장
        cartRepository.save(cart);

        // 응답 DTO 반환
        return new CartResponseDto(
                cart.getRestaurant().getId(),
                cart.getItems(),
                cart.getTotalPrice()
        );
    }

//    @Transactional(readOnly = true)
//    public CartResponseDto getCart(User user) {
//        // 유저의 장바구니 조회
//        Cart cart = cartRepository.findByUser(user)
//                .orElseThrow(() -> new IllegalArgumentException(ExceptionCode.CART_NOT_FOUND.getMessage()));
//
//        // 장바구니 만료 체크
//        if (cart.isCartExpired()) {
//            cartRepository.delete(cart); // 만료된 경우 장바구니 삭제
//            throw new IllegalArgumentException(ExceptionCode.CART_EXPIRED.getMessage()); // 예외 처리
//        }
//
//        // 장바구니 응답 DTO 반환
//        return new CartResponseDto(
//                cart.getRestaurant(),
//                cart.getItems(),
//                cart.getTotalPrice()
//        );
//    }
//
//    @Transactional
//    public void deleteCart(User user) {
//        // 유저의 장바구니 조회
//        Cart cart = cartRepository.findByUser(user)
//                .orElseThrow(() -> new IllegalArgumentException(ExceptionCode.CART_NOT_FOUND.getMessage()));
//
//        // 장바구니 삭제
//        cartRepository.delete(cart);
//    }
//
//    // 새로운 장바구니 생성 메서드
//    private Cart createNewCart(User user) {
//        Cart newCart = new Cart();
//        newCart.setUser(user);
//        newCart.setCreatedAt(LocalDateTime.now());
//        return newCart;
//    }
}