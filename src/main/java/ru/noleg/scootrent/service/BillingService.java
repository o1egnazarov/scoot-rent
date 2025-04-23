package ru.noleg.scootrent.service;

import ru.noleg.scootrent.entity.user.User;

import java.math.BigDecimal;
import java.time.Duration;

public interface BillingService {
    BigDecimal calculateRentalCost(User user, Duration rideDuration);
}
