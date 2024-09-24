package com.sparta.spring26.domain.menu.dto;

import com.sparta.spring26.domain.menu.entity.MenuStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMenuRequestDto {
    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Integer price;

    @NotBlank
    private String category;

    private boolean popularity;

    @NotBlank
    private String status;
}
