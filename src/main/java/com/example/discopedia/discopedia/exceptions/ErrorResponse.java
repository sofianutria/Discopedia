package com.example.discopedia.discopedia.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp,
        int status,
        String error,
        Object message,
        String path) {

}
