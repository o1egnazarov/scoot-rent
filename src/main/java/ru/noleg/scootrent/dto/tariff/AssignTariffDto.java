package ru.noleg.scootrent.dto.tariff;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Назначение специального тарифа")
public record AssignTariffDto(
        @Schema(description = "Id пользователя", example = "1") @NotNull
        Long userId,

        @Schema(description = "Специальная цена за минуту", example = "5.25")
        BigDecimal customPricePerUnit,

        @Schema(description = "Специальная скидка (в процентах)", example = "20")
        @Min(1)
        @Max(100)
        Integer discountPct
) {
}
