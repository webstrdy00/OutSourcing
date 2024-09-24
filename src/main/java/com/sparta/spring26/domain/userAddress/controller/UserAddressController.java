package com.sparta.spring26.domain.userAddress.controller;

import com.sparta.spring26.domain.userAddress.dto.AddressRequestDto;
import com.sparta.spring26.domain.userAddress.dto.AddressResponseDto;
import com.sparta.spring26.domain.userAddress.service.UserAddressService;
import com.sparta.spring26.global.dto.ApiResponse;
import com.sparta.spring26.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery/users/address")
@RequiredArgsConstructor
public class UserAddressController {
    private final UserAddressService userAddressService;

    /**
     * 사용자 주소 목록 조회
     *
     * @param userDetails
     * @return
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getUserAddressList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<AddressResponseDto> addressList = userAddressService.getUserAddressList(userDetails.getUser().getId());
        return ResponseEntity.ok(ApiResponse.success(addressList));
    }

    /**
     * 새로운 주소 추가
     *
     * @param userDetails
     * @param requestDto
     * @return 추가된 주소 정보
     */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> addUserAddress(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody AddressRequestDto requestDto) {
        AddressResponseDto addAddress = userAddressService.addUserAddress(userDetails.getUser().getId(), requestDto.getAddress());
        return ResponseEntity.ok(ApiResponse.success(addAddress));
    }

    /**
     * 주소 삭제
     *
     * @param userDetails
     * @param addressId
     * @return 상태 코드 200
     */
    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<?>> deleteUserAddress(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long addressId) {
        userAddressService.deleteUserAddress(userDetails.getUser().getId(), addressId);
        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }

    /**
     * 유저 기본 주소 설정
     *
     * @param userDetails
     * @param addressId
     * @return 상태 코드 200
     */
    @PutMapping("/{addressId}/primary")
    public ResponseEntity<ApiResponse<?>> setPrimaryAddress(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long addressId) {
        userAddressService.setPrimaryAddress(userDetails.getUser().getId(), addressId);
        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }
}
