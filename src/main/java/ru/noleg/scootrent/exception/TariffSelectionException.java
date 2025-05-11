package ru.noleg.scootrent.exception;

public class TariffSelectionException extends RuntimeException {
    public TariffSelectionException(String message) {
        super(message);
    }

    public TariffSelectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
