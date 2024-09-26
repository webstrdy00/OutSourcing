package com.sparta.spring26.global.security;

import com.sparta.spring26.domain.token.entity.RefreshToken;
import com.sparta.spring26.domain.token.repository.RefreshTokenRedisRepository;
import com.sparta.spring26.domain.user.enums.UserRole;
import com.sparta.spring26.global.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, RefreshTokenRedisRepository refreshTokenRedisRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.refreshTokenRedisRepository = refreshTokenRedisRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        // 로그인과 회원가입 요청은 이 필터를 건너뜁니다.
        String path = req.getRequestURI();
        if (path.equals("/delivery/users/login") || path.equals("/delivery/users/signup")) {
            filterChain.doFilter(req, res);
            return;
        }

        String accessToken = jwtUtil.getJwtFromHeader(req);
        log.info("Extracted accessToken from header: {}", accessToken);
        Optional<String> refreshTokenOpt = jwtUtil.getRefreshTokenFromCooke(req);

        if (StringUtils.hasText(accessToken)) {
            if (jwtUtil.validateToken(accessToken)) {
                setAuthentication(jwtUtil.getUserInfoFromToken(accessToken).getSubject());
            } else if (refreshTokenOpt.isPresent() && jwtUtil.validateRefreshToken(refreshTokenOpt.get())) {
                // AccessToken 이 만료되었지만 RefreshToken 이 유효한 경우
                String refreshToken = refreshTokenOpt.get();
                String email = jwtUtil.getUserInfoFromToken(refreshToken).getSubject();
                Optional<RefreshToken> tokenOpt = refreshTokenRedisRepository.findById(email);

                if (tokenOpt.isPresent() && tokenOpt.get().getToken().equals(refreshToken)) {
                    UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);
                    UserRole role = userDetails.getUser().getRole();

                    String newAccessToken = jwtUtil.createAccessToken(email, role);
                    res.addHeader(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.BEARER_PREFIX + newAccessToken);

                    RefreshToken newRefreshToken = jwtUtil.updateRefreshToken(email, role);
                    jwtUtil.setRefreshTokenCookie(res, newRefreshToken);

                    setAuthentication(email);
                }
            }

            filterChain.doFilter(req, res);
        }
    }

    // 인증 처리
    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}

