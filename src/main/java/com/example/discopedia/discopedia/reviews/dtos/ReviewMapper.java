package com.example.discopedia.discopedia.reviews.dtos;

import com.example.discopedia.discopedia.musicrecords.MusicRecord;
import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponseShort;
import com.example.discopedia.discopedia.reviews.Review;
import com.example.discopedia.discopedia.users.User;
import com.example.discopedia.discopedia.users.dtos.UserResponseShort;

public class ReviewMapper {
    public static Review toEntity(ReviewRequest dto, User user, MusicRecord musicRecord){
        return Review.builder()
                .qualification(dto.qualification())
                .reviewDescription(dto.reviewDescription())
                .user(user)
                .musicRecord(musicRecord)
                .build();
    }

    public static ReviewResponse toDto(Review review){
        UserResponseShort userResponseShort = new UserResponseShort(
                review.getUser().getId(), review.getUser().getUsername());
        MusicRecordResponseShort musicRecordResponseShort = new MusicRecordResponseShort(
                review.getMusicRecord().getTitle(), review.getMusicRecord().getArtist(), review.getMusicRecord().getMusicalGenre()
        );
        return new ReviewResponse(
                review.getId(),
                review.getQualification(),
                review.getReviewDescription(),
                userResponseShort,
                musicRecordResponseShort

        );
    }
}
