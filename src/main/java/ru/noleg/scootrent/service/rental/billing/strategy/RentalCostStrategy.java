package ru.noleg.scootrent.service.rental.billing.strategy;

import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.entity.user.User;

import java.math.BigDecimal;
import java.time.Duration;

public interface RentalCostStrategy {
    BigDecimal calculate(User user, Tariff tariff, Duration rentalDuration);

    TariffType getSupportedTariffType();
}
