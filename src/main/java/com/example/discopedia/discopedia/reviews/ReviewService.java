package com.example.discopedia.discopedia.reviews;

import com.example.discopedia.discopedia.exceptions.EntityAlreadyExistsException;
import com.example.discopedia.discopedia.exceptions.EntityNotFoundException;
import com.example.discopedia.discopedia.musicrecords.MusicRecord;
import com.example.discopedia.discopedia.musicrecords.MusicRecordRepository;
import com.example.discopedia.discopedia.musicrecords.MusicRecordService;
import com.example.discopedia.discopedia.reviews.dtos.ReviewMapper;
import com.example.discopedia.discopedia.reviews.dtos.ReviewRequest;
import com.example.discopedia.discopedia.reviews.dtos.ReviewResponse;
import com.example.discopedia.discopedia.users.User;
import com.example.discopedia.discopedia.users.UserRepository;
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
}
