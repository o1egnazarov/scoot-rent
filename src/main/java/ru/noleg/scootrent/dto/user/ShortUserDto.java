package ru.noleg.scootrent.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Краткая информация о пользователе")
public record ShortUserDto(
        @Schema(description = "Id пользователя", example = "1")
        Long id,

        @Schema(description = "Имя пользователя", example = "user123")
        String username,

        @Schema(description = "Телефон пользователя", example = "+79991226777")
        String phone
) {
}
