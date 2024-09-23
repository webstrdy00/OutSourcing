package com.sparta.spring26.domain.userAddress.service;

import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.domain.user.repository.UserRepository;
import com.sparta.spring26.domain.userAddress.dto.AddressResponseDto;
import com.sparta.spring26.domain.userAddress.entity.UserAddress;
import com.sparta.spring26.domain.userAddress.repository.UserAddressRepository;
import com.sparta.spring26.global.exception.CustomException;
import com.sparta.spring26.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAddressService {
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    
    // 사용자 주소 목록 조회
    @Transactional(readOnly = true)
    public List<AddressResponseDto> getUserAddressList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return user.getAddressList().stream()
                .map(AddressResponseDto::new)
                .collect(Collectors.toList());
    }
    
    // 새로운 주소 추가
    public AddressResponseDto addUserAddress(Long userId, String address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.getAddressList().stream().anyMatch(a -> a.getAddress().equals(address))){
            throw new CustomException(ErrorCode.DUPLICATE_ADDRESS);
        }

        UserAddress newAddress = new UserAddress(address, user);
        user.addAddress(newAddress);
        userAddressRepository.save(newAddress);

        return new AddressResponseDto(newAddress);
    }

    // 기본 주소 설정
    public void setPrimaryAddress(Long userId, Long addressId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserAddress address = user.getAddressList().stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

        user.setPrimaryAddress(address);
    }

    // 주소 삭제
    public void deleteUserAddress(Long userId, Long addressId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserAddress addressToDelete = user.getAddressList().stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

        if (addressToDelete.isPrimary()){
            List<UserAddress> remainAddress = user.getAddressList().stream()
                    .filter(a -> !a.getId().equals(addressId))
                    .collect(Collectors.toList());

            if (!remainAddress.isEmpty()){
                UserAddress newPrimaryAddress = remainAddress.get(0);
                newPrimaryAddress.markAsPrimary();
            }
        }

        user.removeAddress(addressToDelete);
        userAddressRepository.delete(addressToDelete);
    }
}
