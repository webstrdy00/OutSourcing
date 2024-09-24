package com.sparta.spring26.review.servicetest;

import com.sparta.spring26.domain.menu.entity.Menu;
import com.sparta.spring26.domain.order.OrderStatus;
import com.sparta.spring26.domain.order.entity.Order;
import com.sparta.spring26.domain.order.repository.OrderRepository;
import com.sparta.spring26.domain.restaurant.entity.Restaurant;
import com.sparta.spring26.domain.review.dto.request.ReviewRequestDto;
import com.sparta.spring26.domain.review.dto.response.ReviewResponseDto;
import com.sparta.spring26.domain.review.entity.Review;
import com.sparta.spring26.domain.review.repository.ReviewRepository;
import com.sparta.spring26.domain.review.service.ReviewService;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.global.exception.ErrorCode;
import com.sparta.spring26.global.exception.ExceptionCode;
import org.aspectj.util.Reflection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User user;
    private Order order;
    private Review review;
    private Restaurant restaurant;
    private Menu menu;
    private ReviewRequestDto requestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        ReflectionTestUtils.setField(user,"id", 1L);
        ReflectionTestUtils.setField(user,"name", "testUser");

        order = new Order();
        ReflectionTestUtils.setField(order,"id", 1L);
        order.setUser(user);
        order.setStatus(OrderStatus.DELIVERYED);

        restaurant = new Restaurant();
        ReflectionTestUtils.setField(restaurant,"id",1L);
        ReflectionTestUtils.setField(restaurant, "name","Test Restaurant");

        menu = new Menu();
        ReflectionTestUtils.setField(menu, "id", 1L);
        ReflectionTestUtils.setField(menu, "name", "Test Menu");

        review = new Review();
        review.setId(1L);
        review.setUser(user);
        review.setRestaurant(restaurant);
        review.setMenu(menu);
        review.setOrder(order);



        requestDto = new ReviewRequestDto();
        ReflectionTestUtils.setField(requestDto,"contents", "test review");
        ReflectionTestUtils.setField(requestDto, "rating", 5);
        ReflectionTestUtils.setField(requestDto, "menuId", 1L);
        ReflectionTestUtils.setField(requestDto, "restaurantId", 1L);
    }

    @Test
    void 리뷰_등록_성공() {
        when(orderRepository.findByUserIdAndMenuId(anyLong(), anyLong())).thenReturn(Optional.of(order));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewResponseDto responseDto = reviewService.createReview(user, requestDto);

        assertNotNull(responseDto);
        assertEquals(review.getId(),responseDto.getId());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void 리뷰등록_주문_찾기_실패() {
        when(orderRepository.findByUserIdAndMenuId(anyLong(), anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reviewService.createReview(user, requestDto);
        });

        assertEquals(ExceptionCode.ORDER_RECORD_NOT_FOUND.getMessage(), exception.getMessage());

    }

    @Test
    void 리뷰_작성_성공_배달완료() {
        // Given - 배달 완료 상태의 주문 설정
        order.setStatus(OrderStatus.DELIVERYED);  // 배달 완료 상태

        // When
        when(orderRepository.findByUserIdAndMenuId(anyLong(), anyLong())).thenReturn(Optional.of(order));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // Then
        ReviewResponseDto responseDto = reviewService.createReview(user, requestDto);

        assertNotNull(responseDto);
        assertEquals(review.getId(), responseDto.getId());
        verify(reviewRepository, times(1)).save(any(Review.class));  // 리뷰가 저장되는지 확인
    }

    @Test
    void 리뷰_작성_실패_배달미완료() {
        // Given - 배달 완료 상태가 아닌 주문 설정
        order.setStatus(OrderStatus.ORDER_RECEIVED);  // 예시로 주문 접수 상태로 설정

        // When
        when(orderRepository.findByUserIdAndMenuId(anyLong(), anyLong())).thenReturn(Optional.of(order));

        // Then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> reviewService.createReview(user, requestDto));

        assertEquals(ExceptionCode.REVIEW_CANNOT_BE_CREATED.getMessage(), exception.getMessage());
        verify(reviewRepository, never()).save(any(Review.class));  // 리뷰가 저장되지 않음을 확인
    }

    @Test
    void 리뷰수정_성공() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));

        ReviewResponseDto responseDto = reviewService.updateReview(user, review.getId(), requestDto);

        assertNotNull(responseDto);
        assertEquals(review.getId(), responseDto.getId());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void 리뷰수정_권한없음() {
        User otherUser = new User();
        ReflectionTestUtils.setField(otherUser,"id", 2L);
        review.setUser(otherUser);

        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reviewService.updateReview(user, review.getId(), requestDto);
        });

        assertEquals(ExceptionCode.REVIEW_NOT_AUTHORIZED.getMessage(), exception.getMessage());
    }

    @Test
    void 리뷰조회_성공() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));

        ReviewResponseDto responseDto = reviewService.getReview(review.getId());

        assertNotNull(responseDto);
        assertEquals(review.getId(), responseDto.getId());
    }

    @Test
    void 리뷰조회_조회된_리뷰가_없음() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reviewService.getReview(1L);
        });

        assertEquals(ExceptionCode.REVIEW_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void 리뷰_다건_조회_성공() {
        Pageable pageable = PageRequest.of(0,10);
        Page<Review> reviewPage = new PageImpl<>(Collections.singletonList(review));

        when(reviewRepository.findAll(pageable)).thenReturn(reviewPage);

        Page<ReviewResponseDto> reviewList = reviewService.getReviewList(pageable);

        assertEquals(1, reviewList.getContent().size());
    }

    @Test
    void 리뷰삭제_성공() {
        when(reviewRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(review));

        ReviewResponseDto responseDto = reviewService.deleteReview(review.getId(), user);

        assertNotNull(responseDto);
        verify(reviewRepository, times(1)).delete(any(Review.class));
    }

    @Test
    void 리뷰삭제_권한없음() {
        when(reviewRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reviewService.deleteReview(review.getId(), user);
        });

        assertEquals(ExceptionCode.REVIEW_NOT_AUTHORIZED.getMessage(), exception.getMessage());
    }


}
