package com.sparta.spring26.domain.review.repository;

import com.sparta.spring26.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByIdAndUserId(Long reviewId, Long userId);

}
