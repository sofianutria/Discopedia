package com.example.discopedia.discopedia.musicrecords.dtos;

public record MusicRecordResponse(
        Long id,
        String title,
        String artist,
        String musicalGenre,
        Integer year,
        String setlist,
        String imageUrl,
        String user
) {
}
