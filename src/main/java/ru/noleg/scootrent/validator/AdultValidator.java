package ru.noleg.scootrent.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import ru.noleg.scootrent.validator.annotation.Adult;

import java.time.LocalDate;

@Component
public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {

    public static final int AGE_OF_MAJORITY = 18;

    @Override
    public void initialize(Adult constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext context) {
        if (dateOfBirth == null) {
            return true;
        }

        return dateOfBirth.isBefore(LocalDate.now().minusYears(AGE_OF_MAJORITY));
    }
}
