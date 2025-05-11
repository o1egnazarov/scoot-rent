package ru.noleg.scootrent.service.rental.tariffselect.strategy;

import ru.noleg.scootrent.entity.tariff.Tariff;

public interface TariffSelectionStrategy {
    int getPriority();
    Tariff selectTariffForUser(Long userId);
}
