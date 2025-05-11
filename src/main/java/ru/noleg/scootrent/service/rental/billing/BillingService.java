package ru.noleg.scootrent.service.rental.billing;

import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.user.User;

import java.math.BigDecimal;
import java.time.Duration;

public interface BillingService {
    BigDecimal calculateRentalCost(Rental rental, Duration rentalDuration);
}
