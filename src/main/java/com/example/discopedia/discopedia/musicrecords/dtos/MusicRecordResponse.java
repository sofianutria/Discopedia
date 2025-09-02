package com.example.discopedia.discopedia.musicrecords.dtos;

import com.example.discopedia.discopedia.users.dtos.UserResponse;

public record MusicRecordResponse(
        Long id,
        String title,
        String artist,
        String musicalGenre,
        Integer year,
        String setlist,
        String imageUrl,
        UserResponse user
) {
}
