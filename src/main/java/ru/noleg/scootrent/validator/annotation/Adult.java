package ru.noleg.scootrent.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.noleg.scootrent.validator.AdultValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AdultValidator.class)
public @interface Adult {

    String message() default "Invalid age. Only adult users are accepted.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
