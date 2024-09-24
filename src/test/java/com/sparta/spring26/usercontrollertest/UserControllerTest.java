package com.sparta.spring26.usercontrollertest;

import com.sparta.spring26.domain.user.controller.UserController;
import com.sparta.spring26.domain.user.dto.LoginRequestDto;
import com.sparta.spring26.domain.user.dto.UserRequestDto;
import com.sparta.spring26.domain.user.dto.UserResponseDto;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.domain.user.enums.UserRole;
import com.sparta.spring26.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void signup_성공() throws Exception {
        // given
        UserRequestDto requestDto = new UserRequestDto();
        ReflectionTestUtils.setField(requestDto, "email", "test@example.com");
        ReflectionTestUtils.setField(requestDto, "name", "Test User");
        ReflectionTestUtils.setField(requestDto, "password", "securePassword123!");
        ReflectionTestUtils.setField(requestDto, "phone", "010-1234-5678");
        ReflectionTestUtils.setField(requestDto, "owner", false);
        ReflectionTestUtils.setField(requestDto, "ownerToken", "");

        // User 객체 생성
        User user = new User(); // 빈 생성자로 생성

        // Reflection으로 필드 값을 설정
        ReflectionTestUtils.setField(user, "email", requestDto.getEmail());
        ReflectionTestUtils.setField(user, "name", requestDto.getName());
        ReflectionTestUtils.setField(user, "password", "encodedPassword");

        // UserRole 필드 설정 (오류 원인 제거)
        ReflectionTestUtils.setField(user, "role", UserRole.USER); // 여기서 role 필드 설정

        UserResponseDto responseDto = new UserResponseDto(user);

        when(userService.signup(any(UserRequestDto.class))).thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Test User\", \"email\": \"test@example.com\", \"password\": \"securePassword123!\", \"phone\": \"010-1234-5678\", \"owner\": false, \"ownerToken\": \"\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }


    @Test
    void login_성공() throws Exception {
        // given
        LoginRequestDto requestDto = new LoginRequestDto();
        ReflectionTestUtils.setField(requestDto, "email", "test@example.com");
        ReflectionTestUtils.setField(requestDto, "password", "securePassword123!");

        Long userId = 1L;
        when(userService.login(any(LoginRequestDto.class))).thenReturn(userId);

        // when & then
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"email\": \"test@example.com\", \"password\": \"securePassword123!\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(userId));
    }


}
