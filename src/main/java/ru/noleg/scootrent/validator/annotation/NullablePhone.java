package ru.noleg.scootrent.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.noleg.scootrent.validator.NullablePhoneValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullablePhoneValidator.class)
public @interface NullablePhone {

    String message() default "Invalid phone format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}