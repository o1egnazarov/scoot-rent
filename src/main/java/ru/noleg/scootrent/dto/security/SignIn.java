package ru.noleg.scootrent.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.noleg.scootrent.validator.annotation.CorrectUsername;

@Schema(description = "Аутентификация пользователя")
public record SignIn(
        @Schema(description = "Уникальное имя пользователя", example = "user123")
        @NotBlank @CorrectUsername @Size(min = 5, max = 50)
        String username,

        @Schema(description = "Пароль пользователя", example = "user_password123") @NotBlank
        String password
) {
}
