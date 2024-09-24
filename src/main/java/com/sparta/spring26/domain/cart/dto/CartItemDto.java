package com.sparta.spring26.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

    private Long menuId;          // 메뉴 ID
    private String menuName;      // 메뉴 이름
    private int quantity;         // 수량
    private int price;            // 단가
    private int totalPrice;       // 항목별 총 가격 (단가 * 수량)
}
