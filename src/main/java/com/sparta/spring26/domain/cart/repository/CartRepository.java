package com.sparta.spring26.domain.cart.repository;

import com.sparta.spring26.domain.cart.entity.Cart;
import com.sparta.spring26.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUser(User user);
}
