package ru.noleg.scootrent.dto.tariff;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.noleg.scootrent.entity.tariff.DurationType;
import ru.noleg.scootrent.entity.tariff.TariffType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Обновление тарифа")
public record UpdateTariffDto(
        @Schema(description = "Название тарифа", example = "Бесплатные выходные") @NotBlank @Size(min = 5, max = 50)
        String title,

        @Schema(description = "Тип тарифа", example = "SPECIAL_TARIFF") @NotNull
        TariffType type,

        @Schema(description = "Цена за минуту (если подписка, то null)", example = "5.25")
        BigDecimal pricePerUnit,

        @Schema(description = "Цена старта", example = "5")
        int unlockFee,

        @Schema(description = "Единица периода действия тарифа", example = "WEEK") @NotNull
        DurationType durationUnit,

        @Schema(description = "Количество единиц периода", example = "10") @NotNull
        Integer durationValue,

        @Schema(description = "Количество дней в подписке", example = "30")
        Integer subDurationDays,

        @Schema(description = "Начало действия тарифа", example = "2025-05-01T16:51:45.6864356") @NotNull
        LocalDateTime validFrom,

        @Schema(description = "Окончание действия тарифа", example = "2025-12-01T16:51:45.6864356") @NotNull
        LocalDateTime validUntil
) {
}
