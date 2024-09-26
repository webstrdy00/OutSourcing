package com.sparta.spring26.domain.token.entity;

import com.sparta.spring26.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@RedisHash("refreshToken")
public class RefreshToken {
    @Id
    private String id;   // 사용자의 email을 id로 사용

    private String token;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiryDate;

    public RefreshToken(){}

    public RefreshToken(String id, String token, Long expiryDate) {
        this.id = id;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    // Getter 및 Setter 메서드
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Long expiryDate) {
        this.expiryDate = expiryDate;
    }

    // 토큰 업데이트 메서드
    public void updateToken(String token, Long expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
    }
}
