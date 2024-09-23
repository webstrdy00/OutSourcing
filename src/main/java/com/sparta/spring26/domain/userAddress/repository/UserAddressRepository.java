package com.sparta.spring26.domain.userAddress.repository;

import com.sparta.spring26.domain.userAddress.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
}
