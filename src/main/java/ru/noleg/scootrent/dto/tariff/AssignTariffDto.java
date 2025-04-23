package ru.noleg.scootrent.dto.tariff;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AssignTariffDto(@NotNull Long userId,
                              BigDecimal customPricePerUnit,
                              Integer discountPct) {
}
