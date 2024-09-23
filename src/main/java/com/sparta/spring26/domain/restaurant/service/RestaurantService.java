package com.sparta.spring26.domain.restaurant.service;

import com.sparta.spring26.domain.restaurant.dto.request.RestaurantRequestDto;
import com.sparta.spring26.domain.restaurant.dto.request.RestaurantUpdateDto;
import com.sparta.spring26.domain.restaurant.dto.response.RestaurantResponseDto;
import com.sparta.spring26.domain.restaurant.dto.response.RestaurantResponseListDto;
import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import com.sparta.spring26.domain.restaurant.enums.RestaurantCategory;
import com.sparta.spring26.domain.restaurant.enums.RestaurantStatus;
import com.sparta.spring26.domain.restaurant.repository.RestaurantRepository;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.domain.user.entity.UserRole;
import com.sparta.spring26.domain.user.repository.UserRepository;
import com.sparta.spring26.global.exception.CustomException;
import com.sparta.spring26.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    // 가게 생성
    public RestaurantResponseDto createRestaurant(RestaurantRequestDto requestDto, User user) {
        Long userId = user.getId();

        if (!user.getRole().equals(UserRole.OWNER)) {
            throw new CustomException(ErrorCode.NOT_OWNER);
        }

        long openRestaurantsCount = restaurantRepository.countByOwnerIdAndStatus(userId, RestaurantStatus.OPEN);
        if (openRestaurantsCount >= 3) {
            throw new CustomException(ErrorCode.MAX_RESTAURANT_LIMIT);
        }

        Restaurant restaurant = new Restaurant(requestDto, user);
        Restaurant saveRestaurant = restaurantRepository.save(restaurant);

        return RestaurantResponseDto.fromEntity(saveRestaurant);
    }

    // 가게 정보 수정
    public RestaurantResponseDto updateRestaurantPartial(Long restaurantsId, RestaurantUpdateDto updateDto, User user) {
        Long userId = user.getId();

        Restaurant restaurant = restaurantRepository.findById(restaurantsId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESTAURANT_NOT_FOUND));

        if (!restaurant.getOwner().getId().equals(userId)){
            throw new CustomException(ErrorCode.NOT_RESTAURANT_OWNER);
        }

        restaurant.updatePartial(updateDto);

        return RestaurantResponseDto.fromEntity(restaurant);
    }
    
    // 가게 단건 조회
    @Transactional(readOnly = true)
    public RestaurantResponseDto getRestaurant(Long restaurantsId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantsId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESTAURANT_NOT_FOUND));

        return RestaurantResponseDto.fromEntity(restaurant);
    }
    
    // 가게 목록 조회
    @Transactional(readOnly = true)
    public Page<RestaurantResponseListDto> getRestaurantList(String name, RestaurantCategory category, Pageable pageable) {
        Page<Restaurant> restaurantPage;
        if (name != null && !name.isEmpty()){
            if (category != null){
                restaurantPage = restaurantRepository.findByNameContainingIgnoreCaseAndCategoryAndStatus(name, category, RestaurantStatus.OPEN, pageable);
            }else {
                restaurantPage = restaurantRepository.findByNameContainingIgnoreCaseAndStatus(name, RestaurantStatus.OPEN, pageable);
            }
            restaurantPage = restaurantRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (category != null) {
            restaurantPage = restaurantRepository.findByCategoryAndStatus(category, RestaurantStatus.OPEN, pageable);
        } else {
            restaurantPage = restaurantRepository.findAllByStatus(RestaurantStatus.OPEN, pageable);
        }

        return restaurantPage.map(RestaurantResponseListDto::fromEntity);
    }

    // 가게 폐업
    public void closeRestaurant(Long restaurantsId, User user) {
        Long userId = user.getId();
        Restaurant restaurant = restaurantRepository.findById(restaurantsId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESTAURANT_NOT_FOUND));

        if (!restaurant.getOwner().getId().equals(userId)){
            throw new CustomException(ErrorCode.NOT_RESTAURANT_OWNER);
        }

        restaurant.close();
    }
}
