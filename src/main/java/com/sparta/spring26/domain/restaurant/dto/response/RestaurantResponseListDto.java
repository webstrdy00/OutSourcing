package com.sparta.spring26.domain.restaurant.dto.response;

import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantResponseListDto {
    private Long id;
    private String name;
    private String description;
    private Integer minDeliveryPrice;

    public static RestaurantResponseListDto fromEntity(Restaurant restaurant){
        RestaurantResponseListDto dto = new RestaurantResponseListDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setDescription(restaurant.getDescription());
        dto.setMinDeliveryPrice(restaurant.getMinDeliveryPrice());

        return dto;
    }
}
