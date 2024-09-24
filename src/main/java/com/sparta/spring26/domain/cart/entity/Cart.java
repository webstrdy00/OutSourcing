//package com.sparta.spring26.domain.cart.entity;
//
//import com.sparta.spring26.domain.menu.entity.Menu;
//import com.sparta.spring26.domain.restaurant.entity.Restaurant;
//import com.sparta.spring26.domain.user.entity.User;
//import com.sparta.spring26.global.entity.BaseTimeEntity;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Setter
//@Getter
//@NoArgsConstructor
//public class Cart extends BaseTimeEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "restaurant_id", nullable = false)
//    private Restaurant restaurant;
//
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "cart_id")
//    private List<CartItem> items = new ArrayList<>();
//
//    private int totalPrice;
//
//    private LocalDateTime lastUpdated; // 장바구니 업데이트 시간
//
//    public void addItem(Menu menu, int quantity) {
//        CartItem newItem = new CartItem(menu, quantity);
//        items.clear(); // 기존 가게의 메뉴를 모두 제거
//        items.add(newItem);
//        calculateTotalPrice();
//        lastUpdated = LocalDateTime.now();
//    }
//
//    public void calculateTotalPrice() {
//        this.totalPrice = items.stream()
//                .mapToInt(item -> item.getMenu().getPrice() * item.getQuantity())
//                .sum();
//    }
//
//    // 장바구니가가 하루만 유지되는 조건
//    public boolean isCartExpired() {
//        return lastUpdated.isBefore(LocalDateTime.now().minusDays(1)); // 1일 후 만료
//    }
//
//}
