package ru.noleg.scootrent.exception.handler;

import java.util.List;

public record ExceptionResponse(int status, List<String> message, String cause) {
}