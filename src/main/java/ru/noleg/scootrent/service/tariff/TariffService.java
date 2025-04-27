package ru.noleg.scootrent.service.tariff;

import ru.noleg.scootrent.entity.tariff.Tariff;

import java.util.List;

public interface TariffService {
    Long createTariff(Tariff tariff);

    Tariff getTariff(Long id);

    List<Tariff> getAllTariffs();

    List<Tariff> getActiveTariffs();

    void deactivateTariff(Long id);
}

