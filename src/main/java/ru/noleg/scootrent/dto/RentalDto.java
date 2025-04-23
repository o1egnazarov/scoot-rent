package ru.noleg.scootrent.dto;

import ru.noleg.scootrent.dto.rentalPoint.RentalPointDto;
import ru.noleg.scootrent.dto.scooter.ScooterDto;
import ru.noleg.scootrent.dto.user.UserDto;
import ru.noleg.scootrent.entity.UserSubscription;
import ru.noleg.scootrent.entity.rental.RentalStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RentalDto(Long id,
                        UserDto user,
                        ScooterDto scooter,
                        TariffDto tariff,
                        UserSubscription subscription,
                        RentalPointDto startPoint,
                        RentalPointDto endPoint,
                        RentalStatus rentalStatus,
                        BigDecimal cost,
                        LocalDateTime startTime,
                        LocalDateTime endTime) {
}
