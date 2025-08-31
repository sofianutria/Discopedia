package com.example.discopedia.discopedia.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp,
        int status,
        String error,
        Object message,
        String path) {

        public ErrorResponse (HttpStatus status, Object message, HttpServletRequest req){
                this(LocalDateTime.now(), status.value(), status.name(), message, String.valueOf(req.getRequestURI()));
        }

        public ErrorResponse (HttpStatus status, String error, Object message, HttpServletRequest req){
                this(LocalDateTime.now(), status.value(), error, message, String.valueOf(req.getRequestURI()));
        }

}
