package com.example.discopedia.discopedia.musicrecords.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MusicRecordRequest(
        @NotBlank(message= "Title is required")
        String title,

        @NotBlank(message= "Artist is required")
        String artist,

        @NotBlank(message= "Musical genre is required")
        String musicalGenre,

        @NotNull(message= "Year is required")
        Integer year,

        @NotBlank(message= "Setlist is required")
        String setlist,

        @Pattern(message = "Invalid content type", regexp = "^(https?://.*\\.(png|jpg|jpeg|gif|svg))$")
        String image
) {
}
