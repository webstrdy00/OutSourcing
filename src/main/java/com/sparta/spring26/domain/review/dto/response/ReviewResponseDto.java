package com.sparta.spring26.domain.review.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDto {

    private Long id;
    private String contents;
    private Integer rating;
    private String restaurant;
    private String menuName;
    private String userName;

    public ReviewResponseDto(Long id, String contents, Integer rating, String restaurant, String menuName, String userName) {
        this.id = id;
        this.contents = contents;
        this.rating = rating;
        this.restaurant = restaurant;
        this.menuName = menuName;
        this.userName = userName;
    }

}
