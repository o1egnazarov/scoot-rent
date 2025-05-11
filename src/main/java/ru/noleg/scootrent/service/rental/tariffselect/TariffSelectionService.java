package ru.noleg.scootrent.service.rental.tariffselect;

import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.BillingMode;

public interface TariffSelectionService {
    Tariff selectTariffForUser(Long userId, BillingMode billingMode);
}
