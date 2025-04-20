package ru.noleg.scootrent.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.noleg.scootrent.entity.user.Role;

import java.time.LocalDate;

public record UserDto(
        Long id,
        @NotBlank @Size(min = 10, max = 50) String username,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 10, max = 16) String phone,
        @NotNull LocalDate dateOfBirth,
        Role role) {
}
