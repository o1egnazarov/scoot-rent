package ru.noleg.scootrent.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Профиль пользователя")
public record UserProfileDto(
        @Schema(description = "Имя пользователя", example = "user123")
        String username,

        @Schema(description = "Email пользователя", example = "example@gmail.com")
        String email,

        @Schema(description = "Телефон пользователя", example = "+79991226777")
        String phone,

        @Schema(description = "Дата рождения пользователя", example = "2004-08-08")
        LocalDate dateOfBirth
) {
}
