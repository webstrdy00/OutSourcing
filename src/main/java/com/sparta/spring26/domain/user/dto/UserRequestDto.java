package com.sparta.spring26.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRequestDto {
    private String name;

    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[\\p{Punct}])[A-Za-z\\d\\p{Punct}]{8,20}$",
            message = "Password must be 8-20 characters long and include at least one letter, one number, and one special character.")
    private String password;

    private String phone;

    private List<String> addresses;

    private boolean owner = false;
    private String ownerToken = "";
}
