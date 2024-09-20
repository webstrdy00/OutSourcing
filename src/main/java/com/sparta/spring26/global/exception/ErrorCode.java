package com.sparta.spring26.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    NOT_OWNER(HttpStatus.FORBIDDEN, "User is not an owner"),
    MAX_RESTAURANT_LIMIT(HttpStatus.BAD_REQUEST, "Owner has reached the maximum limit of restaurants"),
    NOT_RESTAURANT_OWNER(HttpStatus.FORBIDDEN, "User is not the owner of this restaurant"),

    // 400 BAD_REQUEST: 잘못된 요청
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "파라미터 값을 확인해주세요."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // 401 UNAUTHORIZED: 인증되지 않은 사용자
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "현재 내 계정 정보가 존재하지 않습니다."),

    // 403 FORBIDDEN: 권한이 없는 사용자
    FORBIDDEN_USER(HttpStatus.FORBIDDEN, "해당 요청에 대한 권한이 없습니다."),

    // 404 NOT_FOUND: 리소스를 찾을 수 없음
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다."),
    RESTAURANT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 식당 정보를 찾을 수 없습니다."),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메뉴 정보를 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "로그아웃 된 사용자입니다."),

    // 409 CONFLICT: 중복된 리소스
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일이 존재합니다."),
    ALREADY_DEACTIVATED_USER(HttpStatus.CONFLICT, "이미 탈퇴된 회원입니다."),

    // 500 INTERNAL_SERVER_ERROR: 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 연결에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
