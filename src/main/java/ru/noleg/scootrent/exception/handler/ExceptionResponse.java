package ru.noleg.autoworkshoprest.util.handler;

import java.util.List;

public record ExceptionResponse(int status, List<String> message, String cause) {
}