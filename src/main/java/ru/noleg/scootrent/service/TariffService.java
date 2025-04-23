package ru.noleg.scootrent.service;

import ru.noleg.scootrent.entity.tariff.Tariff;

import java.util.List;

public interface TariffService {
    Long createTariff(Tariff tariff);

    Tariff getTariff(Long id);

    List<Tariff> getAllTariffs();

    List<Tariff> getActiveTariffs();

    Tariff updateTariff(Long id, Tariff tariff);

    void deactivateTariff(Long id);
}

