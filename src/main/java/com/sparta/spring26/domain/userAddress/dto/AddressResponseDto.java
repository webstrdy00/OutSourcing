package com.sparta.spring26.domain.userAddress.dto;

import com.sparta.spring26.domain.userAddress.entity.UserAddress;
import lombok.Getter;

@Getter
public class AddressResponseDto {
    private Long id;
    private String address;
    private boolean isPrimary;

    public AddressResponseDto(UserAddress userAddress) {
        this.id = userAddress.getId();
        this.address = userAddress.getAddress();
        this.isPrimary = userAddress.isPrimary();
    }
}
