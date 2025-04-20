package ru.noleg.scootrent.dto.user;

import jakarta.validation.constraints.Email;

import java.time.LocalDate;

// TODO разобраться с валидацией
public record UpdateUserDto(
        String username,
        @Email String email,
        String phone,
        LocalDate dateOfBirth
) {
}
