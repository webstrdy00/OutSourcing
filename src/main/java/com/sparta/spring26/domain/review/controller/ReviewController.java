package com.sparta.spring26.domain.review.controller;

import com.sparta.spring26.domain.restaurant.dto.response.PagedResponseDto;
import com.sparta.spring26.domain.review.dto.request.ReviewRequestDto;
import com.sparta.spring26.domain.review.dto.response.ReviewPagedResponseDto;
import com.sparta.spring26.domain.review.dto.response.ReviewResponseDto;
import com.sparta.spring26.domain.review.service.ReviewService;
import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery/review")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 등록
     * @param userDetails 인증된 사용자 정보
     * @param reviewRequestDto 리뷰 요청 데이터
     * @return 리뷰 응답 데이터, 상태 코드 201
     */
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ReviewRequestDto reviewRequestDto
    ){
        User user = userDetails.getUser();
        ReviewResponseDto responseDto = reviewService.createReview(user, reviewRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 리뷰 수정
     * @param userDetails 인증된 사용자 정보
     * @param reveiewId 리뷰 ID
     * @param reviewRequestDto 수정할 리뷰 데이터
     * @return 수정된 리뷰 응답 데이터, 상태 코드 200
     */
    @PutMapping("/{reveiewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long reveiewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid
            @RequestBody ReviewRequestDto reviewRequestDto
    ){
        User user = userDetails.getUser();
        ReviewResponseDto responseDto = reviewService.updateReview(user, reveiewId, reviewRequestDto);
        return ResponseEntity.ok(responseDto);

    }

    /**
     * 리뷰 단건 조회
     * @param reviewId 리뷰 ID
     * @return 리뷰 응답 데이터, 상태 코드 200
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReview(@PathVariable Long reviewId){
        ReviewResponseDto review = reviewService.getReview(reviewId);
        return ResponseEntity.ok(review);
    }

    /**
     * 모든 리뷰 조회
     * @param pageable 페이징 정보
     * @return 리뷰 응답 데이터 리스트, 상태 코드 200
     */
    @GetMapping
    public ResponseEntity<ReviewPagedResponseDto<ReviewResponseDto>> getReviewList(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponseDto> reviewPage = reviewService.getReviewList(pageable);

        ReviewPagedResponseDto<ReviewResponseDto> responseDto = new ReviewPagedResponseDto<>(reviewPage);
        return ResponseEntity.ok(responseDto);

    }

    /**
     * 리뷰 삭제
     * @param reviewId 리뷰 ID
     * @param userDetails 인증된 사용자 정보
     * @return 상태 코드 204
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        User user = userDetails.getUser();
        ReviewResponseDto responseDto = reviewService.deleteReview(reviewId, user);
        return ResponseEntity.ok(responseDto);
    }

}
