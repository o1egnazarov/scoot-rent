package ru.noleg.scootrent.dto.tariff;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserTariffDto(
        Long id,
        Long tariffId,
        BigDecimal customPricePerMinute,
        Integer discountPct,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
