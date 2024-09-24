package com.sparta.spring26.domain.user.service;

import com.sparta.spring26.domain.user.dto.LoginRequestDto;
import com.sparta.spring26.domain.user.dto.UserRequestDto;
import com.sparta.spring26.domain.user.dto.UserResponseDto;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.domain.user.enums.UserRole;
import com.sparta.spring26.domain.user.enums.UserStatus;
import com.sparta.spring26.domain.user.repository.UserRepository;
import com.sparta.spring26.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private final String OWNER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Nested
    @DisplayName("회원가입 테스트")
    class SignupTest{
        @Test
        @DisplayName("일반 사용자 회원가입 성공 테스트")
        void signup_User_Success() {
            // given
            UserRequestDto requestDto = new UserRequestDto();
            ReflectionTestUtils.setField(requestDto, "email", "user@test.com");
            ReflectionTestUtils.setField(requestDto, "password", "password123");
            ReflectionTestUtils.setField(requestDto, "name", "Test User");
            ReflectionTestUtils.setField(requestDto, "phone", "01012345678");
            ReflectionTestUtils.setField(requestDto, "owner", false);

            User savedUser = new User();
            ReflectionTestUtils.setField(savedUser, "id", 1L);
            ReflectionTestUtils.setField(savedUser, "email", requestDto.getEmail());
            ReflectionTestUtils.setField(savedUser, "name", requestDto.getName());
            ReflectionTestUtils.setField(savedUser, "role", UserRole.USER);

            given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
            given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
            given(userRepository.save(any(User.class))).willReturn(savedUser);

            // when
            UserResponseDto responseDto = userService.signup(requestDto);

            // then
            assertNotNull(responseDto);
            assertEquals(requestDto.getEmail(), responseDto.getEmail());
            assertEquals(requestDto.getName(), responseDto.getName());
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("점주 회원가입 성공 테스트")
        void signup_Owner_Success() {
            // given
            UserRequestDto requestDto = new UserRequestDto();
            ReflectionTestUtils.setField(requestDto, "email", "owner@test.com");
            ReflectionTestUtils.setField(requestDto, "password", "password123");
            ReflectionTestUtils.setField(requestDto, "name", "Test Owner");
            ReflectionTestUtils.setField(requestDto, "phone", "01087654321");
            ReflectionTestUtils.setField(requestDto, "owner", true);
            ReflectionTestUtils.setField(requestDto, "ownerToken", OWNER_TOKEN);

            User savedUser = new User();
            ReflectionTestUtils.setField(savedUser, "id", 2L);
            ReflectionTestUtils.setField(savedUser, "email", requestDto.getEmail());
            ReflectionTestUtils.setField(savedUser, "name", requestDto.getName());
            ReflectionTestUtils.setField(savedUser, "role", UserRole.OWNER);

            given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
            given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
            given(userRepository.save(any(User.class))).willReturn(savedUser);

            // when
            UserResponseDto responseDto = userService.signup(requestDto);

            // then
            assertNotNull(responseDto);
            assertEquals(requestDto.getEmail(), responseDto.getEmail());
            assertEquals(requestDto.getName(), responseDto.getName());
            verify(userRepository).save(any(User.class));
        }

//        @Test
//        @DisplayName("일반 사용자 회원가입 성공 테스트 - 주소 포함")
//        void signup_User_WithAddress_Success() {
//            // given
//            UserRequestDto requestDto = new UserRequestDto();
//            ReflectionTestUtils.setField(requestDto, "email", "user@test.com");
//            ReflectionTestUtils.setField(requestDto, "password", "password123");
//            ReflectionTestUtils.setField(requestDto, "name", "Test User");
//            ReflectionTestUtils.setField(requestDto, "phone", "01012345678");
//            ReflectionTestUtils.setField(requestDto, "owner", false);
//            List<String> addresses = Arrays.asList("서울시 강남구", "서울시 서초구");
//            ReflectionTestUtils.setField(requestDto, "addresses", addresses);
//
//            User savedUser = new User();
//            ReflectionTestUtils.setField(savedUser, "id", 1L);
//            ReflectionTestUtils.setField(savedUser, "email", requestDto.getEmail());
//            ReflectionTestUtils.setField(savedUser, "name", requestDto.getName());
//            ReflectionTestUtils.setField(savedUser, "role", UserRole.USER);
//
//            UserAddress primaryAddress = new UserAddress("서울시 강남구", savedUser);
//            savedUser.setPrimaryAddress(primaryAddress);
//
//            given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
//            given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
//            given(userRepository.save(any(User.class))).willReturn(savedUser);
//
//            // when
//            UserResponseDto responseDto = userService.signup(requestDto);
//
//            // then
//            assertNotNull(responseDto);
//            assertEquals(savedUser.getId(), responseDto.getId());
//            assertEquals(requestDto.getEmail(), responseDto.getEmail());
//            assertEquals(requestDto.getName(), responseDto.getName());
//            assertEquals("서울시 강남구", responseDto.getPrimaryAddress());
//            verify(userRepository).save(any(User.class));
//        }
        @Test
        @DisplayName("회원가입 실패 테스트 - 이메일 중복")
        void signup_Fail_DuplicateEmail() {
            // given
            UserRequestDto requestDto = new UserRequestDto();
            ReflectionTestUtils.setField(requestDto, "email", "existing@test.com");

            given(userRepository.findByEmail(anyString())).willReturn(Optional.of(new User()));

            // when & then
            assertThrows(IllegalArgumentException.class, () -> userService.signup(requestDto));
        }
        @Test
        @DisplayName("회원가입 실패 테스트 - 잘못된 점주 토큰")
        void signup_Fail_InvalidOwnerToken() {
            // given
            UserRequestDto requestDto = new UserRequestDto();
            ReflectionTestUtils.setField(requestDto, "email", "owner@test.com");
            ReflectionTestUtils.setField(requestDto, "owner", true);
            ReflectionTestUtils.setField(requestDto, "ownerToken", "invalidToken");

            given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

            // when & then
            assertThrows(IllegalArgumentException.class, () -> userService.signup(requestDto));
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTest{
        @Test
        @DisplayName("로그인 성공 테스트")
        void login_Success() {
            // given
            LoginRequestDto requestDto = new LoginRequestDto();
            ReflectionTestUtils.setField(requestDto, "email", "user@test.com");
            ReflectionTestUtils.setField(requestDto, "password", "password123");

            User user = new User();
            ReflectionTestUtils.setField(user, "id", 1L);
            ReflectionTestUtils.setField(user, "email", "user@test.com");
            ReflectionTestUtils.setField(user, "password", "encodedPassword");

            given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

            // when
            Long userId = userService.login(requestDto);

            // then
            assertNotNull(userId);
            assertEquals(1L, userId);
        }
        @Test
        @DisplayName("로그인 실패 테스트 - 사용자 없음")
        void login_Fail_UserNotFound() {
            // given
            LoginRequestDto requestDto = new LoginRequestDto();
            ReflectionTestUtils.setField(requestDto, "email", "nonexistent@test.com");

            given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

            // when & then
            assertThrows(IllegalArgumentException.class, () -> userService.login(requestDto));
        }
        @Test
        @DisplayName("로그인 실패 테스트 - 비밀번호 불일치")
        void login_Fail_InvalidPassword() {
            // given
            LoginRequestDto requestDto = new LoginRequestDto();
            ReflectionTestUtils.setField(requestDto, "email", "user@test.com");
            ReflectionTestUtils.setField(requestDto, "password", "wrongpassword");

            User user = new User();
            ReflectionTestUtils.setField(user, "email", "user@test.com");
            ReflectionTestUtils.setField(user, "password", "encodedPassword");

            given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

            // when & then
            assertThrows(IllegalArgumentException.class, () -> userService.login(requestDto));
        }
    }

    @Nested
    @DisplayName("회원 탈퇴 테스트")
    class DeleteUserTest{
        @Test
        @DisplayName("회원 탈퇴 성공 테스트")
        void deleteUser_Success() {
            // given
            Long userId = 1L;
            String password = "password123";

            User user = new User();
            ReflectionTestUtils.setField(user, "id", userId);
            ReflectionTestUtils.setField(user, "password", "encodedPassword");
            ReflectionTestUtils.setField(user, "status", UserStatus.ACTIVE);

            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(passwordEncoder.matches(password, user.getPassword())).willReturn(true);

            // when
            Long deletedUserId = userService.deleteUser(userId, password);

            // then
            assertEquals(userId, deletedUserId);
            assertEquals(UserStatus.INACTIVE, user.getStatus());
        }

        @Test
        @DisplayName("회원 탈퇴 실패 테스트 - 사용자 없음")
        void deleteUser_Fail_UserNotFound() {
            // given
            Long userId = 1L;
            String password = "password123";

            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when & then
            assertThrows(CustomException.class, () -> userService.deleteUser(userId, password));
        }

        @Test
        @DisplayName("회원 탈퇴 실패 테스트 - 이미 탈퇴한 사용자")
        void deleteUser_Fail_AlreadyDeactivated() {
            // given
            Long userId = 1L;
            String password = "password123";

            User user = new User();
            ReflectionTestUtils.setField(user, "id", userId);
            ReflectionTestUtils.setField(user, "status", UserStatus.INACTIVE);

            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            // when & then
            CustomException exception = assertThrows(CustomException.class, () -> userService.deleteUser(userId, password));
            assertEquals("이미 탈퇴된 회원입니다.", exception.getMessage());
        }

        @Test
        @DisplayName("회원 탈퇴 실패 테스트 - 비밀번호 불일치")
        void deleteUser_Fail_InvalidPassword() {
            // given
            Long userId = 1L;
            String password = "wrongpassword";

            User user = new User();
            ReflectionTestUtils.setField(user, "id", userId);
            ReflectionTestUtils.setField(user, "password", "encodedPassword");
            ReflectionTestUtils.setField(user, "status", UserStatus.ACTIVE);

            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(passwordEncoder.matches(password, user.getPassword())).willReturn(false);

            // when & then
            CustomException exception = assertThrows(CustomException.class, () -> userService.deleteUser(userId, password));
            assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("사용자 정보 조회 테스트")
    class GetUserInfoTest{
        @Test
        @DisplayName("사용자 정보 조회 성공 테스트")
        void getUserInfo_Success() {
            // given
            Long userId = 1L;
            User user = new User();
            ReflectionTestUtils.setField(user, "id", userId);
            ReflectionTestUtils.setField(user, "email", "user@test.com");
            ReflectionTestUtils.setField(user, "name", "Test User");
            ReflectionTestUtils.setField(user, "role", UserRole.USER);

            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            // when
            Object result = userService.getUserInfo(userId);

            // then
            assertTrue(result instanceof UserResponseDto);
            UserResponseDto responseDto = (UserResponseDto) result;
            assertEquals(user.getEmail(), responseDto.getEmail());
            assertEquals(user.getName(), responseDto.getName());
        }
        @Test
        @DisplayName("사용자 정보 조회 실패 테스트 - 사용자 없음")
        void getUserInfo_Fail_UserNotFound() {
            // given
            Long userId = 1L;
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when & then
            assertThrows(CustomException.class, () -> userService.getUserInfo(userId));
        }
    }
}