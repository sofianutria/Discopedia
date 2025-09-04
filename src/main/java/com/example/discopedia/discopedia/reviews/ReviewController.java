package com.example.discopedia.discopedia.reviews;

import com.example.discopedia.discopedia.reviews.dtos.ReviewRequest;
import com.example.discopedia.discopedia.reviews.dtos.ReviewResponse;
import com.example.discopedia.discopedia.security.CustomUserDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReviewResponse> addReview(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @RequestBody @Valid ReviewRequest request) {
        ReviewResponse created = reviewService.addReview(customUserDetail.getUser().getId(), request);
        return ResponseEntity.ok(created);
    }
}
