package com.example.discopedia.discopedia.reviews;

import com.example.discopedia.discopedia.musicrecords.MusicRecordRepository;
import com.example.discopedia.discopedia.users.UserRepository;
import org.springframework.stereotype.Service;

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
}
