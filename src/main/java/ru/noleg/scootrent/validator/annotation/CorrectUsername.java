package ru.noleg.scootrent.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.noleg.scootrent.validator.CorrectUsernameValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CorrectUsernameValidator.class)
public @interface CorrectUsername {
    String message() default "Invalid username. Correct username: username123, user_name, user-name, user123_name-";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}