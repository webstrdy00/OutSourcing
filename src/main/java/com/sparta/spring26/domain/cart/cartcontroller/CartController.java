//package com.sparta.spring26.domain.cart.cartcontroller;
//
//import com.sparta.spring26.domain.cart.dto.CartListDto;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/delivery/cart")
//@RequiredArgsConstructor
//public class CartController {
//
//    private final CartService cartService;
//
//    // 장바구니 추가
//    @PostMapping
//    public CartListDto addCart(CartDto cartDto, long storeId, String storeName, int deliveryTip, HttpSession session) {
//        return cartService.addCart(cartDto, storeId, storeName, deliveryTip, session);
//    }
//
//    // 장바구니 목록 조회
//    @GetMapping
//    public CartListDto getCartList(HttpSession session) {
//        return cartService.getCartList(session);
//    }
//
//    // 장바구니 전체 삭제
//    @DeleteMapping
//    public void deleteAllCart(HttpSession session) {
//        cartService.deleteAllCart(session);
//    }
//
//    // 장바구니 한 개 삭제
//    @DeleteMapping("/{index}")
//    public CartListDto deleteOneCart(@PathVariable int index, HttpSession session) {
//        return cartService.deleteOneCart(index, session);
//    }
//}
