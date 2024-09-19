package com.sparta.spring26.domain.restaurant.service;

import com.sparta.spring26.domain.restaurant.dto.request.RestaurantRequestDto;
import com.sparta.spring26.domain.restaurant.dto.request.RestaurantUpdateDto;
import com.sparta.spring26.domain.restaurant.dto.response.RestaurantResponseDto;
import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import com.sparta.spring26.domain.restaurant.repository.RestaurantRepository;
import com.sparta.spring26.global.exception.CustomException;
import com.sparta.spring26.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
//    private final UserRepository userRepository;

    // 가게 생성
    public RestaurantResponseDto createRestaurant(RestaurantRequestDto requestDto /**, Long userId **/) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
//
//        if (!user.getRole().equals(UserRole.OWNER)) {
//            throw new CustomException(ErrorCode.NOT_OWNER);
//        }
//        if (restaurantRepository.countByOwnerId(userId) >= 3) {
//            throw new CustomException(ErrorCode.MAX_RESTAURANT_LIMIT);
//        }

        Restaurant restaurant = new Restaurant(requestDto);
        Restaurant saveRestaurant = restaurantRepository.save(restaurant);

        return RestaurantResponseDto.fromEntity(saveRestaurant);
    }

    // 가게 정보 수정
    public RestaurantResponseDto updateRestaurantPartial(Long restaurantsId, RestaurantUpdateDto updateDto/**, Long userId**/) {
        Restaurant restaurant = restaurantRepository.findById(restaurantsId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESTAURANT_NOT_FOUND));

//        if (!restaurant.getOwner().getId().equals(userId)){
//            throw new CustomException(ErrorCode.NOT_RESTAURANT_OWNER);
//        }

        restaurant.updatePartial(updateDto);

        return RestaurantResponseDto.fromEntity(restaurant);
    }
}
