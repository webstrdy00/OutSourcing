package com.sparta.spring26.domain.user.dto;

import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.domain.user.entity.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private UserRole userRole;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public UserResponseDto(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.userRole = user.getRole();
        this.createAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}
