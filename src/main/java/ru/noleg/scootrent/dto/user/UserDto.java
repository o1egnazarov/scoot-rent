package ru.noleg.scootrent.dto.user;

import ru.noleg.scootrent.entity.user.Role;

import java.time.LocalDate;

public record UserDto(
        Long id,
        String username,
        String email,
        String phone,
        LocalDate dateOfBirth,
        Role role) {
}
