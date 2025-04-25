package ru.noleg.scootrent.exception.handler;

import java.time.LocalDateTime;
import java.util.List;

public record ExceptionResponse(LocalDateTime timestamp, List<String> message, String cause) {
}