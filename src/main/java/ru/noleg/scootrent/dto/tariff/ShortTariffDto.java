package ru.noleg.scootrent.dto.tariff;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShortTariffDto(Long id,
                             String title,
                             BigDecimal pricePerUnit,
                             int unlockFee,
                             Integer subDurationDays,
                             LocalDateTime validFrom,
                             LocalDateTime validUntil) {
}
