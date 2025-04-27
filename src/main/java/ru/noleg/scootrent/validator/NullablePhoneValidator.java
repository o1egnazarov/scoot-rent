package ru.noleg.scootrent.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import ru.noleg.scootrent.validator.annotation.NullablePhone;

@Component
public class NullablePhoneValidator implements ConstraintValidator<NullablePhone, String> {

    @Override
    public void initialize(NullablePhone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null) {
            return true;
        }

        return phone.matches("^\\+?[0-9]{10,15}$");
    }
}