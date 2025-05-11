package ru.noleg.scootrent.dto.tariff;

import ru.noleg.scootrent.entity.tariff.TariffType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShortTariffDto(Long id,
                             String title,
                             BigDecimal pricePerUnit,
                             int unlockFee,
                             TariffType type,
                             Integer subDurationDays,
                             LocalDateTime validFrom,
                             LocalDateTime validUntil) {
}
