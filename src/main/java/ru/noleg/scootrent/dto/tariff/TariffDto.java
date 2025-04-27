package ru.noleg.scootrent.dto.tariff;

import ru.noleg.scootrent.entity.tariff.DurationType;
import ru.noleg.scootrent.entity.tariff.TariffType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TariffDto(Long id,
                        String title,
                        TariffType type,
                        BigDecimal pricePerUnit,
                        int unlockFee,
                        DurationType durationUnit,
                        Integer durationValue,
                        Integer subDurationDays,
                        LocalDateTime validFrom,
                        LocalDateTime validUntil) {
}