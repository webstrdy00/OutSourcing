//package com.sparta.spring26.domain.cart.entity;
//
//import com.sparta.spring26.domain.menu.entity.Menu;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//public class CartItem {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    private Menu menu;
//
//    private int quantity;
//
//}