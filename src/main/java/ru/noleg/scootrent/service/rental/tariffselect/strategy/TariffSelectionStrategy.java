package ru.noleg.scootrent.service.rental.tariffselect.strategy;

import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.BillingMode;

public interface TariffSelectionStrategy {
    int getPriority();
    Tariff selectTariff(Long userId, BillingMode billingMode);
}
