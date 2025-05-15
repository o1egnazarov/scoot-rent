package ru.noleg.scootrent.dto.tariff;

import java.time.LocalDateTime;

public record UserSubscriptionDto(
        Long id,
        Long tariffId,
        int minuteUsageLimit,
        int minutesUsed,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}