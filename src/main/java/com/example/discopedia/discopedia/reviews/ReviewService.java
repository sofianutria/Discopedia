package com.example.discopedia.discopedia.reviews;

import com.example.discopedia.discopedia.exceptions.EntityAlreadyExistsException;
import com.example.discopedia.discopedia.exceptions.EntityNotFoundException;
import com.example.discopedia.discopedia.musicrecords.MusicRecord;
import com.example.discopedia.discopedia.musicrecords.MusicRecordRepository;
import com.example.discopedia.discopedia.reviews.dtos.ReviewMapper;
import com.example.discopedia.discopedia.reviews.dtos.ReviewRequest;
import com.example.discopedia.discopedia.reviews.dtos.ReviewResponse;
import com.example.discopedia.discopedia.users.Role;
import com.example.discopedia.discopedia.users.User;
import com.example.discopedia.discopedia.users.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MusicRecordRepository musicRecordRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, MusicRecordRepository musicRecordRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.musicRecordRepository = musicRecordRepository;
    }

    public List<ReviewResponse> getReviewsByMusicRecord(Long musicRecordId){
        MusicRecord musicRecord = musicRecordRepository.findById(musicRecordId)
                .orElseThrow(()->new EntityNotFoundException("Music record", "id", musicRecordId.toString()));

        return reviewRepository.findByMusicRecord(musicRecord)
                .stream()
                .map(ReviewMapper::toDto)
                .toList();
    }

    @PreAuthorize("isAuthenticated()")
    public ReviewResponse addReview(Long userId, ReviewRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", "id", userId.toString()));
        MusicRecord musicRecord = musicRecordRepository.findById(request.musicRecordId())
                .orElseThrow(() -> new EntityNotFoundException("Music record", "id", request.musicRecordId().toString()));
        boolean exists = reviewRepository.existsByUserAndMusicRecord(user, musicRecord);
        if (exists) {
            throw new EntityAlreadyExistsException("Review", "userId + musicRecordId", user.getId() + " & " + musicRecord.getId());
        }
        Review review = ReviewMapper.toEntity(request, user, musicRecord);
        reviewRepository.save(review);
        return ReviewMapper.toDto(review);
    }

    @PreAuthorize("isAuthenticated()")
    public ReviewResponse updateReview(Long id, ReviewRequest request, User user) {
        Review review = findReviewOrThrow(id);
        assertUserIsOwner(review, user);

        review.setQualification(request.qualification());
        review.setReviewDescription(request.reviewDescription());

        Review updated = reviewRepository.save(review);
        return ReviewMapper.toDto(updated);
    }

    @PreAuthorize("isAuthenticated()")
    public String deleteReview(Long id, User user) {
        Review review = findReviewOrThrow(id);
        assertUserIsOwnerOrAdmin(review, user);

        reviewRepository.delete(review);
        return "Review with id " + id + " deleted successfully";
    }

    public void assertUserIsOwner(Review review, User user) {
        if (!review.getUser().getId().equals(user.getId()) ) {
            throw new AccessDeniedException("You are not authorized to modify or delete this review.");
        }
    }

    private Review findReviewOrThrow(Long id) {
        return reviewRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Review", "id", id.toString()));
    }

    public void assertUserIsOwnerOrAdmin(Review review, User user) {
        if (!review.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("You are not authorized to modify or delete this review");
        }
    }
}
