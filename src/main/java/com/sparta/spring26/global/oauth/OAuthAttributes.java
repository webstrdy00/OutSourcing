package com.sparta.spring26.global.oauth;

import java.util.Map;

public class OAuthAttributes {
    public static UserInfo extractKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return new UserInfo(
                String.valueOf(attributes.get("id")),
                (String) kakaoProfile.get("nickname"),
                (String) kakaoAccount.get("email")
        );
    }
}
