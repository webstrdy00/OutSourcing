package com.sparta.spring26.domain.restaurant.controller;

import com.sparta.spring26.domain.restaurant.dto.request.RestaurantRequestDto;
import com.sparta.spring26.domain.restaurant.dto.request.RestaurantUpdateDto;
import com.sparta.spring26.domain.restaurant.dto.response.PagedResponseDto;
import com.sparta.spring26.domain.restaurant.dto.response.RestaurantResponseDto;
import com.sparta.spring26.domain.restaurant.dto.response.RestaurantResponseListDto;
import com.sparta.spring26.domain.restaurant.service.RestaurantService;
import com.sparta.spring26.domain.user.entity.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    /**
     * 가게 생성 코드
     * @param requestDto
     * @param request
     * @return RestaurantResponseDto, 상태코드 201
     */
//    @Secured(UserRole.Authority.OWNER)   // 사장 권한만 접근 가능( Security 구현시 주석해제)
    @PostMapping
    public ResponseEntity<RestaurantResponseDto> createRestaurant(@Valid @RequestBody RestaurantRequestDto requestDto, HttpServletRequest request){
//        Long userId = (Long) request.getAttribute("userId");      // 필터 구현시 주석 해제
        RestaurantResponseDto createRestaurant = restaurantService.createRestaurant(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createRestaurant);
    }

    /**
     * 가게 정보 수정 코드
     * @param restaurantsId
     * @param updateDto
     * @param request
     * @return RestaurantResponseDto, 상태코드 200
     */
//    @Secured(UserRole.Authority.OWNER)   // 사장 권한만 접근 가능( Security 구현시 주석해제)
    @PatchMapping ("/{restaurantsId}")
    public ResponseEntity<RestaurantResponseDto> updateRestaurantPartial(@PathVariable Long restaurantsId, @RequestBody RestaurantUpdateDto updateDto, HttpServletRequest request){
//        Long userId = (Long) request.getAttribute("userId");      // 필터 구현시 주석 해제
        RestaurantResponseDto updateRestaurant = restaurantService.updateRestaurantPartial(restaurantsId, updateDto);
        return ResponseEntity.ok(updateRestaurant);
    }

    /**
     * 가게 단건 조회
     * @param restaurantsId
     * @return 상태 코드 200, 가게 정보
     */
    @GetMapping("{restaurantsId}")
    public ResponseEntity<RestaurantResponseDto> getRestaurant(@PathVariable Long restaurantsId){
        RestaurantResponseDto restaurant = restaurantService.getRestaurant(restaurantsId);
        return ResponseEntity.ok(restaurant);
    }

    /**
     * 가게 목록 조회
     * @param name
     * @param page
     * @param size
     * @return 상태 코드 200, 가게 정보들
     */
    @GetMapping
    public ResponseEntity<PagedResponseDto<RestaurantResponseListDto>> getRestaurantList(@RequestParam(required = false) String name,
                                                                                         @RequestParam(defaultValue = "0")int page,
                                                                                         @RequestParam(defaultValue = "10")int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<RestaurantResponseListDto> restaurantPage = restaurantService.getRestaurantList(name, pageable);
        return ResponseEntity.ok(new PagedResponseDto<>(restaurantPage));
    }
}
