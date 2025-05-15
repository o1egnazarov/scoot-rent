package ru.noleg.scootrent.dto.tariff;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.entity.tariff.TariffType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Краткая информация о тарифе")
public record ShortTariffDto(
        @Schema(description = "Id тарифа", example = "1")
        Long id,

        @Schema(description = "Название тарифа", example = "Бесплатные выходные")
        String title,

        @Schema(description = "Цена за единицу времени (если подписка, то null)", example = "5.25")
        BigDecimal pricePerUnit,

        @Schema(description = "Цена старта", example = "5")
        int unlockFee,

        @Schema(description = "Тип тарифа", example = "SPECIAL_TARIFF")
        TariffType type,

        @Schema(description = "Тарификация почасовая/поминутная", example = "PER_MINUTE")
        BillingMode billingMode,

        @Schema(description = "Количество дней в подписке", example = "30")
        Integer subDurationDays,

        @Schema(description = "Начало действия тарифа", example = "2025-05-01T16:51:45.6864356")
        LocalDateTime validFrom,

        @Schema(description = "Окончание действия тарифа", example = "2025-12-01T16:51:45.6864356")
        LocalDateTime validUntil
) {
}
