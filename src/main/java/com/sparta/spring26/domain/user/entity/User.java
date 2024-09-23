package com.sparta.spring26.domain.user.entity;

import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import com.sparta.spring26.domain.user.dto.UserRequestDto;
import com.sparta.spring26.domain.user.enums.UserRole;
import com.sparta.spring26.domain.user.enums.UserStatus;
import com.sparta.spring26.domain.userAddress.entity.UserAddress;
import com.sparta.spring26.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30)
    private String phone;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAddress> addressList = new ArrayList<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Restaurant> restaurantList = new ArrayList<>();

    public User(UserRequestDto requestDto, String password, UserRole role) {
        this.email = requestDto.getEmail();
        this.name = requestDto.getName();
        this.phone = requestDto.getPhone();
        this.password = password;
        this.role = role;
        this.status = UserStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = UserStatus.INACTIVE;
    }

    public void suspend() {
        this.status = UserStatus.SUSPENDED;
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
    }

    public void addAddress(UserAddress address) {
        this.addressList.add(address);
        address.setUser(this);
    }

    public void removeAddress(UserAddress address) {
        this.addressList.remove(address);
        address.setUser(null);
    }

    public void setPrimaryAddress(UserAddress newPrimaryAddress){
        addressList.forEach(address ->{
            if (address.equals(newPrimaryAddress)){
                address.markAsPrimary();
            }else {
                address.unmarkAsPrimary();
            }
        });
    }

    public UserAddress getPrimaryAddress(){
        return addressList.stream()
                .filter(UserAddress::isPrimary)
                .findFirst()
                .orElse(null);
    }
}
