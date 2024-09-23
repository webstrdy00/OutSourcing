package com.sparta.spring26.domain.user.controller;

import com.sparta.spring26.domain.user.dto.LoginRequestDto;
import com.sparta.spring26.domain.user.dto.UserDeleteRequestDto;
import com.sparta.spring26.domain.user.dto.UserRequestDto;
import com.sparta.spring26.domain.user.dto.UserResponseDto;
import com.sparta.spring26.domain.user.service.UserService;
import com.sparta.spring26.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    /**
     * 회원 가입
     * @param requestDto
     * @return 상태코드 200, UserResponseDto
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody UserRequestDto requestDto){
        return ResponseEntity.ok(userService.signup(requestDto));
    }

    /**
     * 로그인 코드
     * @param requestDto
     * @return 상태코드 200, userId
     */
    @PostMapping("/login")
    public ResponseEntity<Long> login(@RequestBody LoginRequestDto requestDto){
        return ResponseEntity.ok(userService.login(requestDto));
    }

    /**
     * 회원 탈퇴 코드
     * @param userDetails
     * @param requestDto
     * @return 삳태코드 200, userId
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Long> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserDeleteRequestDto requestDto){
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(userService.deleteUser(userId, requestDto.getPassword()));
    }
}
