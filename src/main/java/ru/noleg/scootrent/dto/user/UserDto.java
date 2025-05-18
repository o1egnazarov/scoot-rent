package ru.noleg.scootrent.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.noleg.scootrent.entity.user.Role;

import java.time.LocalDate;

@Schema(description = "Пользователь")
public record UserDto(
        @Schema(description = "Id пользователя", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Имя пользователя", example = "user123")
        String username,

        @Schema(description = "Email пользователя", example = "example@gmail.com")
        String email,

        @Schema(description = "Телефон пользователя", example = "+79991226777")
        String phone,

        @Schema(description = "Дата рождения пользователя", example = "2004-08-08")
        LocalDate dateOfBirth,

        @Schema(description = "Роль пользователя", example = "ROLE_USER")
        Role role
) {
}
