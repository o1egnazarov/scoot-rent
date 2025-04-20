package ru.noleg.scootrent.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

// TODO добавить кастомный валидатор
public record SignUp(
        @NotBlank @Size(min = 5, max = 50) String username,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "\\+?[0-9]{7,15}") String phone,
        @NotBlank String password,
        LocalDate dateOfBirthday
) {
}
