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
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성 (로그인한 유저만 가능, 특정 레스토랑 및 메뉴에 대한 리뷰만 작성 가능)
    @PostMapping
    public ResponseEntity<?> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ReviewRequestDto reviewRequestDto
    ){
        Long userId = userDetails.getUser().getId();
        ReviewResponseDto responseDto = reviewService.createReview(userId, reviewRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 리뷰 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reveiewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid
            @RequestBody ReviewRequestDto reviewRequestDto
    ){
        Long userId = userDetails.getUser().getId();
        ReviewResponseDto responseDto = reviewService.updateReview(userId, reveiewId, reviewRequestDto);
        return ResponseEntity.ok(responseDto);

    }

    // 리뷰 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getReview(@PathVariable Long reviewId){
        ReviewResponseDto review = reviewService.getReview(reviewId);
        return ResponseEntity.ok(review);
    }

    // 리뷰 다건 조회(페이징)
    @GetMapping
    public ResponseEntity<ReviewPagedResponseDto<ReviewResponseDto>> getReviewList(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponseDto> reviewPage = reviewService.getReviewList(pageable);

        ReviewPagedResponseDto<ReviewResponseDto> responseDto = new ReviewPagedResponseDto<>(reviewPage);
        return ResponseEntity.ok(responseDto);

    }

    // 리뷰 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> deleteReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto reviewRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        User user = userDetails.getUser();
        ReviewResponseDto responseDto = reviewService.deleteReview(reviewId, reviewRequestDto, user);
        return ResponseEntity.ok(responseDto);
    }

}
