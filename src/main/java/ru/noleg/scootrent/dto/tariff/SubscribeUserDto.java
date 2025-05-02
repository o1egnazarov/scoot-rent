package ru.noleg.scootrent.dto.tariff;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Назначение подписки")
public record SubscribeUserDto(
        @Schema(description = "Id пользователя") @NotNull Long userId
) {
}
