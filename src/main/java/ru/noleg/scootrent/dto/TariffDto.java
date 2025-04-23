package ru.noleg.scootrent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.noleg.scootrent.entity.tariff.DurationType;
import ru.noleg.scootrent.entity.tariff.TariffType;

import java.time.LocalDateTime;

public record TariffDto(
        Long id,
        @NotBlank @Size(min = 5, max = 50) String title,
        @NotNull TariffType type,
        Integer pricePerUnit,
        int unlockFee,
        DurationType durationUnit,
        Integer durationValue,
        Integer subDurationDays,
        LocalDateTime validFrom,
        LocalDateTime validUntil
) {
}