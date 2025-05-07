package ru.noleg.scootrent.exception.handler;

import java.time.LocalDateTime;

public record ExceptionResponse(ErrorCode code,
                                String message,
                                String path,
                                LocalDateTime timestamp) {
}