package com.sparta.spring26.domain.kakao.controller;

import com.sparta.spring26.domain.kakao.dto.KakaoLoginRequest;
import com.sparta.spring26.domain.kakao.dto.TokenResponse;
import com.sparta.spring26.domain.kakao.service.KakaoService;
import com.sparta.spring26.global.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery/oauth")
public class KakaoController {

    private final KakaoService kakaoService;

    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/kakao")
    public ResponseEntity<ApiResponse<String>> getKakaoLoginUrl(){
        String kakaoLoginUrl = kakaoService.getKakaoLoginUrl();
        return ResponseEntity.ok(ApiResponse.success(kakaoLoginUrl));
    }

    @PostMapping("/kakao/login")
    public ResponseEntity<ApiResponse<?>> kakaoLogin(@RequestBody KakaoLoginRequest request){
        String accessToken = kakaoService.kakaoLogin(request.getAuthorizationCode());

        return ResponseEntity.ok(ApiResponse.success(new TokenResponse(accessToken)));
    }
}
