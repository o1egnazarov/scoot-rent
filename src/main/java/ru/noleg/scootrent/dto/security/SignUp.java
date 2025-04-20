package ru.noleg.scootrent.dto;

import java.time.LocalDate;

public record SignUp(
        String username,
        String email,
        LocalDate dateOfBirthday,
        String password
) {
}
