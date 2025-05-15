package ru.noleg.scootrent.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ru.noleg.scootrent.validator.annotation.Adult;
import ru.noleg.scootrent.validator.annotation.CorrectUsername;

import java.time.LocalDate;

@Schema(description = "Регистрация пользователя")
public record SignUp(
        @Schema(description = "Уникальное имя пользователя", example = "user123")
        @NotBlank @CorrectUsername @Size(min = 5, max = 50)
        String username,

        @Schema(description = "Уникальное email пользователя", example = "user123@gmail.com") @NotBlank @Email
        String email,

        @Schema(description = "Уникальный номер телефона пользователя", example = "+79541180360")
        @NotBlank @Pattern(regexp = "^\\+?[0-9]{10,15}$")
        String phone,

        @Schema(description = "Пароль пользователя", example = "user_password123") @NotBlank
        String password,

        @Schema(description = "Дата рождения пользователя", example = "2000-09-18")
        @NotNull @Adult LocalDate dateOfBirth
) {
}
