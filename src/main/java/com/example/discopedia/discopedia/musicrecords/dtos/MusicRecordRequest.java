package com.example.discopedia.discopedia.musicrecords.dtos;

import jakarta.validation.constraints.NotBlank;

public record MusicRecordRequest(
        @NotBlank(message= "Title is required")
        String title,

        @NotBlank(message= "Artist is required")
        String artist
) {
}
