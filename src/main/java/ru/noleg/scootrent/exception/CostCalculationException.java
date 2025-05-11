package ru.noleg.scootrent.exception;

public class CostCalculationException extends RuntimeException {
    public CostCalculationException(String message) {
        super(message);
    }

    public CostCalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}
