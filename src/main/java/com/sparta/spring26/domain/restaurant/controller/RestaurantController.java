package com.sparta.spring26.domain.restaurant.controller;

import com.sparta.spring26.domain.restaurant.dto.request.RestaurantRequestDto;
import com.sparta.spring26.domain.restaurant.dto.request.RestaurantUpdateDto;
import com.sparta.spring26.domain.restaurant.dto.response.PagedResponseDto;
import com.sparta.spring26.domain.restaurant.dto.response.RestaurantResponseDto;
import com.sparta.spring26.domain.restaurant.dto.response.RestaurantResponseListDto;
import com.sparta.spring26.domain.restaurant.enums.RestaurantCategory;
import com.sparta.spring26.domain.restaurant.service.RestaurantService;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.domain.user.enums.UserRole;
import com.sparta.spring26.global.dto.ApiResponse;
import com.sparta.spring26.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    /**
     * 가게 생성 코드
     *
     * @param requestDto
     * @param userDetails
     * @return RestaurantResponseDto, 상태코드 201
     */
    @Secured(UserRole.Authority.OWNER)   // 사장 권한만 접근 가능
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createRestaurant(@Valid @RequestBody RestaurantRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, BindingResult bindingResult) {
        try {
            User user = userDetails.getUser();
            RestaurantResponseDto createRestaurant = restaurantService.createRestaurant(requestDto, user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(createRestaurant));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail(bindingResult));
        }
    }

    /**
     * 가게 정보 수정 코드
     *
     * @param restaurantsId
     * @param updateDto
     * @param userDetails
     * @return RestaurantResponseDto, 상태코드 200
     */
    @Secured(UserRole.Authority.OWNER)   // 사장 권한만 접근 가능
    @DeleteMapping("/{restaurantsId}")
    public ResponseEntity<ApiResponse<?>> updateRestaurantPartial(@PathVariable Long restaurantsId, @RequestBody RestaurantUpdateDto updateDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        RestaurantResponseDto updateRestaurant = restaurantService.updateRestaurantPartial(restaurantsId, updateDto, user.getId());
        return ResponseEntity.ok(ApiResponse.success(updateRestaurant));
    }

    /**
     * 가게 단건 조회
     *
     * @param restaurantsId
     * @return 상태 코드 200, 가게 정보
     */
    @GetMapping("{restaurantsId}")
    public ResponseEntity<ApiResponse<?>> getRestaurant(@PathVariable Long restaurantsId) {
        RestaurantResponseDto restaurant = restaurantService.getRestaurant(restaurantsId);
        return ResponseEntity.ok(ApiResponse.success(restaurant));
    }

    /**
     * 가게 목록 조회
     *
     * @param name
     * @param page
     * @param size
     * @return 상태 코드 200, 가게 정보들
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getRestaurantList(@RequestParam(required = false) String name,
                                                            @RequestParam(required = false) RestaurantCategory category,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantResponseListDto> restaurantPage = restaurantService.getRestaurantList(name, category, pageable);
        return ResponseEntity.ok(ApiResponse.success(new PagedResponseDto<>(restaurantPage)));
    }

    /**
     * 가게 폐업
     *
     * @param restaurantsId
     * @param userDetails
     * @return
     */
    @Secured(UserRole.Authority.OWNER)   // 사장 권한만 접근 가능
    @PatchMapping("/{restaurantsId}/close")
    public ResponseEntity<ApiResponse<?>> closeRestaurant(@PathVariable Long restaurantsId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        restaurantService.closeRestaurant(restaurantsId, user.getId());
        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }
}
