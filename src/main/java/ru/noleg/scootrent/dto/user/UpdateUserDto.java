package ru.noleg.scootrent.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import ru.noleg.scootrent.validator.annotation.Adult;
import ru.noleg.scootrent.validator.annotation.CorrectUsername;
import ru.noleg.scootrent.validator.annotation.NullablePhone;

import java.time.LocalDate;

@Schema(description = "Обновление пользователя")
public record UpdateUserDto(
        @Schema(description = "Имя пользователя", example = "user123") @CorrectUsername @Size(max = 50)
        String username,

        @Schema(description = "Email пользователя", example = "example@gmail.com") @Email @Size(max = 50)
        String email,

        @Schema(description = "Телефон пользователя", example = "+79991226777") @NullablePhone @Size(max = 16)
        String phone,

        @Schema(description = "Дата рождения пользователя", example = "2004-08-08") @Adult
        LocalDate dateOfBirth) {
}
