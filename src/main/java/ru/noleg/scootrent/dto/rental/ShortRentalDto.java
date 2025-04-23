package ru.noleg.scootrent.dto.rental;

import ru.noleg.scootrent.dto.rentalPoint.ShortRentalPointDto;
import ru.noleg.scootrent.dto.scooter.ShortScooterDtoWithModel;
import ru.noleg.scootrent.dto.tariff.ShortTariffDto;
import ru.noleg.scootrent.dto.user.ShortUserDto;
import ru.noleg.scootrent.entity.UserSubscription;
import ru.noleg.scootrent.entity.rental.RentalStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShortRentalDto(Long id,
                             ShortUserDto user,
                             ShortScooterDtoWithModel scooter,
                             ShortTariffDto tariff,
                             UserSubscription subscription,
                             ShortRentalPointDto startPoint,
                             ShortRentalPointDto endPoint,
                             RentalStatus rentalStatus,
                             BigDecimal cost,
                             LocalDateTime startTime,
                             LocalDateTime endTime) {
}
