package ru.noleg.scootrent.service.rental.billing;

import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.user.User;

import java.math.BigDecimal;
import java.time.Duration;

public interface BillingService {
    BigDecimal calculateRentalCost(User user, Tariff tariff, Duration rentalDuration);
}
