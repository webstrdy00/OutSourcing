package com.sparta.spring26.domain.restaurant.dto.response;

import com.sparta.spring26.domain.menu.dto.GetMenuResponseDto;
import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import com.sparta.spring26.domain.restaurant.enums.RestaurantCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class RestaurantResponseDto {
    private Long id;
    private String name;
    private String description;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Integer minDeliveryPrice;
    private String address;
    private RestaurantCategory category;
    private List<GetMenuResponseDto> menuList;

    public static RestaurantResponseDto fromEntity(Restaurant restaurant){
        RestaurantResponseDto dto = new RestaurantResponseDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setDescription(restaurant.getDescription());
        dto.setOpenTime(restaurant.getOpenTime());
        dto.setCloseTime(restaurant.getCloseTime());
        dto.setMinDeliveryPrice(restaurant.getMinDeliveryPrice());
        dto.setAddress(restaurant.getAddress());
        dto.setCategory(restaurant.getCategory());
        dto.setMenuList(restaurant.getMenuList().stream()
                .map(GetMenuResponseDto::new)
                .collect(Collectors.toList()));
        return dto;
    }
}
