package ru.noleg.scootrent.service.rental.start;

import ru.noleg.scootrent.entity.tariff.BillingMode;

public interface RentalStarter {
    Long startRental(Long userId, Long scooterId, Long startPointId, BillingMode billingMode);
}
