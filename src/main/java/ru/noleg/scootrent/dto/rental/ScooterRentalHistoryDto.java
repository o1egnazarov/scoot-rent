package ru.noleg.scootrent.dto.scooter;

import ru.noleg.scootrent.entity.rental.RentalStatus;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public record ScooterRentalHistoryDto(
        Long rentalId,
        String username,
        String phone,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Duration duration,
        BigDecimal cost,
        String startPointTitle,
        String endPointTitle,
        String tariffTitle,
        RentalStatus rentalStatus
) {
}