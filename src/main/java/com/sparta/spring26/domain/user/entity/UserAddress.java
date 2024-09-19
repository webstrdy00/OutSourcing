package com.sparta.spring26.domain.user.entity;

import com.sparta.spring26.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "user_address")
public class UserAddress extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
