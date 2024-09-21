package com.sparta.spring26.domain.review.service;

import com.sparta.spring26.domain.order.entity.Order;
import com.sparta.spring26.domain.order.repository.OrderRepository;
import com.sparta.spring26.domain.review.dto.request.ReviewRequestDto;
import com.sparta.spring26.domain.review.dto.response.ReviewResponseDto;
import com.sparta.spring26.domain.review.entity.Review;
import com.sparta.spring26.domain.review.repository.ReviewRepository;
import com.sparta.spring26.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    // 리뷰 등록
    @Transactional
    public ReviewResponseDto createReview(Long userId, ReviewRequestDto requestDto) {

        // Order에서 리뷰를 작성할 수 있는 자격 확인
        Order order = orderRepository.findByUserIdAndMenuId(userId, requestDto.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴에 대한 주문 기록이 없습니다."));

        // 주문 상태 체크
        if (!order.getStatus().canReview()) {
            throw new IllegalArgumentException("리뷰는 배달 완료 상태에서만 작성할 수 있습니다.");
        }

        // 주문한 사용자와 현재 로그인한 사용자가 동일한지 확인
        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 주문에 대한 권한이 없습니다.");
        }


        // 리뷰 작성
        Review review = new Review();
        review.setContents(requestDto.getContents());
        review.setRating(requestDto.getRating());
        review.setMenu(order.getMenu());
        review.setRestaurant(order.getRestaurant());
        review.setUser(order.getUser());

        // 리뷰 저장
        Review savedReview = reviewRepository.save(review);

        return new ReviewResponseDto(
                savedReview.getId(),
                savedReview.getContents(),
                savedReview.getRating(),
                savedReview.getRestaurant().getName(),
                savedReview.getMenu().getName(),
                savedReview.getUser().getName()
        );
    }

    // 리뷰 수정
    @Transactional
    public ReviewResponseDto updateReview(Long userId, Long reviewId, ReviewRequestDto requestDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        // 리뷰 작성자와 수정하려는 사용자가 동일한지 확인
        if (!review.getUser().equals(userId)) {
            throw new IllegalArgumentException("해당 리뷰에 대한 수정 권한이 없습니다.");
        }

        review.setContents(requestDto.getContents());
        review.setRating(requestDto.getRating());

        reviewRepository.save(review);

        return new ReviewResponseDto(
                review.getId(),
                review.getContents(),
                review.getRating(),
                review.getRestaurant().getName(),
                review.getMenu().getName(),
                review.getUser().getName()
        );
    }

    // 리뷰 단건 조회
    @Transactional(readOnly = true)
    public ReviewResponseDto getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        return new ReviewResponseDto(
                review.getId(),
                review.getContents(),
                review.getRating(),
                review.getMenu().getName(),
                review.getMenu().getName(),
                review.getUser().getName()
        );
    }

    // 리뷰 다건 조회
    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> getReviewList(Pageable pageable) {
        return reviewRepository.findAll(pageable)
                .map(review -> new ReviewResponseDto(
                        review.getId(),
                        review.getContents(),
                        review.getRating(),
                        review.getRestaurant().getName(),
                        review.getMenu().getName(),
                        review.getUser().getName()
                ));
    }

    // 리뷰 삭제
    @Transactional
    public ReviewResponseDto deleteReview(Long reviewId, ReviewRequestDto reviewRequestDto, User user) {
        Review review = reviewRepository.findByIdAndUserId(reviewId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없거나 삭제 권한이 없습니다."));

        reviewRepository.delete(review);

        return new ReviewResponseDto(
                review.getId(),
                review.getContents(),
                review.getRating(),
                review.getRestaurant().getName(),
                review.getMenu().getName(),
                review.getUser().getName()
        );
    }
}
