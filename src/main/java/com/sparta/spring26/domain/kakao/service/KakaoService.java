package com.sparta.spring26.domain.kakao.service;

import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.domain.user.enums.UserRole;
import com.sparta.spring26.domain.user.repository.UserRepository;
import com.sparta.spring26.global.jwt.JwtUtil;
import com.sparta.spring26.global.oauth.OAuthAttributes;
import com.sparta.spring26.global.oauth.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoService {
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    public String getKakaoLoginUrl() {
        return "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";
    }

    public String kakaoLogin(String authorizationCode) {
        // 1. 인가 코드로 카카오 액세스 토큰 요청
        String kakaoAccessToken = getKakaoAccessToken(authorizationCode);

        // 2. 카카오 액세스 ㅋ토큰으로 카카오 사용자 정보 요청
        UserInfo userInfo = getKakaoUserInfo(kakaoAccessToken);

        // 3. 카카오 사용자 정보로 회원가입 또는 로그인 처리
        User user = registerOrLogin(userInfo);

        // 4. Jwt accessToken 생성 및 반환
        return jwtUtil.createAccessToken(user.getEmail(), user.getRole());
    }

    // 카카오 액세스 토큰 요청 메서드
    private String getKakaoAccessToken(String authorizationCode) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        // HTTP 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 바디 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                kakaoTokenRequest,
                Map.class
        );

        return (String) response.getBody().get("access_token");
    }

    // 카카오 사용자 정보 요청 메서드
    private UserInfo getKakaoUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        // HTTP 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                kakaoUserInfoRequest,
                Map.class
        );

        // 응답에서 사용자 정보 추출
        Map<String, Object> attributes = response.getBody();
        return OAuthAttributes.extractKakao(attributes);
    }

    // 회원가입 또는 로그인 처리 메서드
    private User registerOrLogin(UserInfo userInfo) {
        return userRepository.findByEmailAndProvider(userInfo.getEmail(), "kakao")
                .map(user -> {
                    // 기존 사용자의 경우 정보 업데이트
                    user.update(userInfo.getName());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    // 새로운 사용자의 경우 회원가입
                    User newUser = new User(
                            userInfo.getName(),
                            userInfo.getEmail(),
                            "kakao",
                            userInfo.getId(),
                            UserRole.USER
                    );
                    return userRepository.save(newUser);
                });
    }
}
