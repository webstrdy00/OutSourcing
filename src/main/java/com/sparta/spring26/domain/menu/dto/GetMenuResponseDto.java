package com.sparta.spring26.domain.menu.dto;

import com.sparta.spring26.domain.menu.entity.Menu;
import com.sparta.spring26.domain.menu.entity.MenuStatus;
import lombok.Getter;

@Getter
public class GetMenuResponseDto {
    private final String name;
    private final Integer price;
    private final String category;
    private final Boolean popularity;
    private final MenuStatus status;

    public GetMenuResponseDto(Menu menu) {
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.category = menu.getCategory();
        this.popularity = menu.isPopularity();
        this.status = menu.getStatus();
    }
}
