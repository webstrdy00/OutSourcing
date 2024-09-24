//package com.sparta.spring26.review.controllertest;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sparta.spring26.domain.menu.entity.Menu;
//import com.sparta.spring26.domain.order.OrderStatus;
//import com.sparta.spring26.domain.order.entity.Order;
//import com.sparta.spring26.domain.restaurant.entity.Restaurant;
//import com.sparta.spring26.domain.review.controller.ReviewController;
//import com.sparta.spring26.domain.review.dto.request.ReviewRequestDto;
//import com.sparta.spring26.domain.review.dto.response.ReviewPagedResponseDto;
//import com.sparta.spring26.domain.review.dto.response.ReviewResponseDto;
//import com.sparta.spring26.domain.review.service.ReviewService;
//import com.sparta.spring26.domain.user.entity.User;
//import com.sparta.spring26.global.security.UserDetailsImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.Arrays;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ReviewController.class)
//class ReviewControllerTest {
//
//    @MockBean
//    private ReviewService reviewService;
//
//    @InjectMocks
//    private ReviewController reviewController;
//
//    private MockMvc mockMvc;
//
//    private ObjectMapper objectMapper;
//
//    private User user;
//    private ReviewRequestDto reviewRequestDto;
//    private ReviewResponseDto reviewResponseDto;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
//        objectMapper = new ObjectMapper();
//
//        // User 초기화
//        user = new User();
//        ReflectionTestUtils.setField(user, "id", 1L);
//        ReflectionTestUtils.setField(user, "name", "testUser");
//
//        // Menu 초기화
//        Menu menu = new Menu();
//        ReflectionTestUtils.setField(menu, "id", 1L);
//        ReflectionTestUtils.setField(menu, "name", "Test Menu");
//        ReflectionTestUtils.setField(menu, "price", 15000);
//
//        // Restaurant 초기화
//        Restaurant restaurant = new Restaurant();
//        ReflectionTestUtils.setField(restaurant, "id", 1L);
//        ReflectionTestUtils.setField(restaurant, "name", "Test Restaurant");
//        ReflectionTestUtils.setField(restaurant, "minDeliveryPrice", 10000);
//
//        // Order 초기화
//        Order order = new Order();
//        ReflectionTestUtils.setField(order, "id", 1L);
//        ReflectionTestUtils.setField(order, "user", user);
//        ReflectionTestUtils.setField(order, "restaurant", restaurant);
//        ReflectionTestUtils.setField(order, "menu", menu);
//        order.setStatus(OrderStatus.DELIVERYED);
//
//        // ReviewRequestDto 초기화
//        reviewRequestDto = new ReviewRequestDto();
//        ReflectionTestUtils.setField(reviewRequestDto, "contents", "Great food!");
//        ReflectionTestUtils.setField(reviewRequestDto, "rating", 5);
//        ReflectionTestUtils.setField(reviewRequestDto, "menuId", menu.getId());
//        ReflectionTestUtils.setField(reviewRequestDto, "restaurantId", restaurant.getId());
//
//        // ReviewResponseDto 초기화
//        reviewResponseDto = new ReviewResponseDto(1L, "test review", 5, restaurant.getName(), menu.getName(), user.getName());
//    }
//
//    @Test
//    void 리뷰_등록_성공() throws Exception {
//        when(reviewService.createReview(any(User.class), any(ReviewRequestDto.class))).thenReturn(reviewResponseDto);
//
//        mockMvc.perform(post("/delivery/review")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(reviewRequestDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.contents").value("Great food!"))
//                .andExpect(jsonPath("$.rating").value(5))
//                .andExpect(jsonPath("$.restaurantName").value("Test Restaurant"))
//                .andExpect(jsonPath("$.menuName").value("Test Menu"))
//                .andExpect(jsonPath("$.userName").value("testUser"));
//    }
//}