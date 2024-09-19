package com.sparta.spring26.domain.menu.controller;

import com.sparta.spring26.domain.menu.dto.CreateMenuRequestDto;
import com.sparta.spring26.domain.menu.dto.GetMenuResponseDto;
import com.sparta.spring26.domain.menu.dto.UpdateMenuRequestDto;
import com.sparta.spring26.domain.menu.entity.Menu;
import com.sparta.spring26.domain.menu.entity.MenuStatus;
import com.sparta.spring26.domain.menu.service.MenuService;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery/restaurants/{restaurantId}/menus")
public class MenuController {
    private final MenuService menuService;

    /**
     * 메뉴 등록
     */
    @PostMapping
//    public ApiResponse<Void> createMenu(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable long restaurantId, @Valid @RequestBody CreateMenuRequestDto request){
//        User user = userDetails.getUser();
    public ApiResponse<Void> createMenu(@PathVariable Long restaurantId, @Valid @RequestBody CreateMenuRequestDto request){
        User user = new User();

        menuService.createMenu(user, restaurantId, request.getName(), request.getCategory(), request.getPrice());

        return ApiResponse.success(
                HttpStatus.CREATED.getReasonPhrase(),
                HttpStatus.CREATED.value(),
                null
        );
    }

    /**
     * 메뉴 수정
     */
    @PatchMapping("/{id}")
//    public ApiResponse<Void> updateMenu(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long restaurantId, @PathVariable Long id, @Valid @RequestBody UpdateMenuRequestDto request){
//        User user = userDetails.getUser();
    public ApiResponse<Void> updateMenu(@PathVariable Long restaurantId, @PathVariable Long id, @Valid @RequestBody UpdateMenuRequestDto request){
        User user = new User();

        menuService.updateMenu(user, restaurantId, id, request.getName(), request.getCategory(), request.getPrice(), request.getPopularity(), MenuStatus.of(request.getStatus().toUpperCase()));

        return ApiResponse.success(
                HttpStatus.OK.getReasonPhrase(),
                HttpStatus.OK.value(),
                null
        );
    }

    /**
     * 메뉴 다건 조회
     */
    @GetMapping
    public ApiResponse<List<GetMenuResponseDto>> getmenus(@PathVariable Long restaurantId){
        List<Menu> menuList = menuService.getMenus(restaurantId);

        List<GetMenuResponseDto> menuResponseDtoList = new ArrayList<>();
        for (Menu menu : menuList) {
            menuResponseDtoList.add(new GetMenuResponseDto(menu));
        }

        return ApiResponse.success(
                HttpStatus.OK.getReasonPhrase(),
                HttpStatus.OK.value(),
                menuResponseDtoList
        );
    }


    /**
     * 메뉴 단건 조회
     */
    @GetMapping("/{id}")
    public ApiResponse<GetMenuResponseDto> getmenu(@PathVariable Long restaurantId, @PathVariable Long id){
        Menu menu = menuService.getMenu(restaurantId, id);

        return ApiResponse.success(
                HttpStatus.OK.getReasonPhrase(),
                HttpStatus.OK.value(),
                new GetMenuResponseDto(menu)
        );
    }
}
