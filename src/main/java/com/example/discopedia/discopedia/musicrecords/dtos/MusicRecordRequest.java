package com.example.discopedia.discopedia.musicrecords.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record MusicRecordRequest(
        @NotBlank(message= "Title is required")
        String title,

        @NotBlank(message= "Artist is required")
        String artist,

        @NotBlank(message= "Musical genre is required")
        String musicalGenre,

        @NotEmpty(message= "Year is required")
        Integer year,

        @NotBlank(message= "Setlist is required")
        String setlist
) {
}
