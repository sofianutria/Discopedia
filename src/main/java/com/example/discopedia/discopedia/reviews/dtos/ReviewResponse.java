package com.example.discopedia.discopedia.reviews.dtos;

import com.example.discopedia.discopedia.musicrecords.dtos.MusicRecordResponse;
import com.example.discopedia.discopedia.users.dtos.UserResponse;

public record ReviewResponse(
        Long id,
        String qualification,
        String reviewDescription,
        UserResponse username,
        MusicRecordResponse title,
        MusicRecordResponse artist
) {
}
