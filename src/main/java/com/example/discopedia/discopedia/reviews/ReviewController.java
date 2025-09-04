package com.example.discopedia.discopedia.reviews;

import com.example.discopedia.discopedia.reviews.dtos.ReviewRequest;
import com.example.discopedia.discopedia.reviews.dtos.ReviewResponse;
import com.example.discopedia.discopedia.security.CustomUserDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/musicrecord/{musicRecordId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByMusicRecord(@PathVariable Long musicRecordId) {
        return ResponseEntity.ok(reviewService.getReviewsByMusicRecord(musicRecordId));
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewResponse> addReview(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @RequestBody @Valid ReviewRequest request) {
        ReviewResponse created = reviewService.addReview(customUserDetail.getUser().getId(), request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewResponse> updateReview(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewRequest request
    ) {
        ReviewResponse updated = reviewService.updateReview(reviewId, request, customUserDetail.getUser());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteReview(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @PathVariable Long reviewId
    ) {
        String message = reviewService.deleteReview(reviewId, customUserDetail.getUser());
        return ResponseEntity.ok(message);
    }
}
