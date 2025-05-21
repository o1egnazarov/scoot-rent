package ru.noleg.scootrent.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.noleg.scootrent.validator.annotation.CorrectUsername;

public class CorrectUsernameValidator implements ConstraintValidator<CorrectUsername, String> {
    @Override
    public void initialize(CorrectUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) {
            return true;
        }

        return username.matches("^[a-zA-Z0-9_-]+$");
    }
}
