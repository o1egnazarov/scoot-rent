package ru.noleg.scootrent.service.rental.tariffselect;

import ru.noleg.scootrent.entity.tariff.Tariff;

public interface TariffSelectionService {
    Tariff selectTariffForUser(Long userId);
}
