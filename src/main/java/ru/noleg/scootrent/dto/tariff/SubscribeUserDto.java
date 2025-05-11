package ru.noleg.scootrent.dto.tariff;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Назначение подписки")
public record SubscribeUserDto(
        @Schema(description = "Id пользователя") @NotNull Long userId,
        @Schema(description = "Лимит использования минут") @NotNull @Min(10) @Max(10000) Integer minutesUsageLimit
) {
}
