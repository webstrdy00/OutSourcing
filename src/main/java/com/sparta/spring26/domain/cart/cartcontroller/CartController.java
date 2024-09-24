package com.sparta.spring26.domain.cart.cartcontroller;

import com.sparta.spring26.domain.cart.dto.request.CartRequestDto;
import com.sparta.spring26.domain.cart.dto.response.CartResponseDto;
import com.sparta.spring26.domain.cart.service.CartService;
import com.sparta.spring26.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponseDto> addItem (
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CartRequestDto cartRequestDto) {
        CartResponseDto cartResponseDto = cartService.addItem(cartRequestDto, userDetails.getUser());
        return ResponseEntity.ok(cartResponseDto);
    }

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        CartResponseDto cartResponseDto = cartService.getCart(userDetails.getUser());
        return ResponseEntity.ok(cartResponseDto);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> deleteCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cartService.deleteCart(userDetails.getUser());
        return ResponseEntity.noContent().build();
    }
}
