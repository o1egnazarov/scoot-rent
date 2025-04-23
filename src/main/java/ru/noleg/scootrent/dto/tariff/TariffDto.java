package ru.noleg.scootrent.dto.tariff;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.noleg.scootrent.entity.tariff.DurationType;
import ru.noleg.scootrent.entity.tariff.TariffType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TariffDto(
        Long id,
        @NotBlank @Size(min = 5, max = 50) String title,
        @NotNull TariffType type,
        BigDecimal pricePerUnit,
        int unlockFee,
        DurationType durationUnit,
        Integer durationValue,
        Integer subDurationDays,
        LocalDateTime validFrom,
        LocalDateTime validUntil
) {
}