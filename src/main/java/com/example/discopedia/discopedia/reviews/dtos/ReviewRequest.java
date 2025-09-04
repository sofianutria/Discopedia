package com.example.discopedia.discopedia.reviews.dtos;

import jakarta.validation.constraints.*;

public record ReviewRequest(
        @NotNull(message= "Qualification is required")
        @Min(value=1, message="Qualification must be at least 1")
        @Max(value=5, message="Qualification can not be more than 5")
        int qualification,

        @NotBlank(message="Review description cannot be empty")
        @Size(max=500, message= "Review description cannot exceed 500 characters")
        String reviewDescription,

        @NotNull (message= "Music record ID is required")
        Long musicRecordId
) {
}
