package com.sparta.spring26.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spring26.domain.token.entity.RefreshToken;
import com.sparta.spring26.domain.user.dto.LoginRequestDto;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.global.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/delivery/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        User user = userDetails.getUser();

        String accessToken = jwtUtil.createAccessToken(user.getEmail(), user.getRole());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.BEARER_PREFIX + accessToken);


        // RefreshToken 생성
        RefreshToken refreshToken = jwtUtil.createRefreshToken(user.getEmail(), user.getRole());

        // RefreshToken을 쿠키에 설정
        jwtUtil.setRefreshTokenCookie(response, refreshToken);

        log.info("로그인 성공: {}", user.getEmail());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.error("Authentication failed: {}", failed.getMessage());
        response.setStatus(401);
    }
}
