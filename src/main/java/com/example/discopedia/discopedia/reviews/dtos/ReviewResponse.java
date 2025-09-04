package com.example.discopedia.discopedia.reviews.dtos;

import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponseShort;
import com.example.discopedia.discopedia.users.dtos.UserResponse;

public record ReviewResponse(
        Long id,
        int qualification,
        String reviewDescription,
        UserResponse username,
        MusicRecordResponseShort musicRecordResponseShort
) {
}
