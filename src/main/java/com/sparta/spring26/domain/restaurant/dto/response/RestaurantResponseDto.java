package com.sparta.spring26.domain.restaurant.dto.response;

import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

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
//    private List<Menu> menuList;

    public static RestaurantResponseDto fromEntity(Restaurant restaurant){
        RestaurantResponseDto dto = new RestaurantResponseDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setDescription(restaurant.getDescription());
        dto.setOpenTime(restaurant.getOpenTime());
        dto.setCloseTime(restaurant.getCloseTime());
        dto.setMinDeliveryPrice(restaurant.getMinDeliveryPrice());
        dto.setAddress(restaurant.getAddress());

        return dto;
    }
}
