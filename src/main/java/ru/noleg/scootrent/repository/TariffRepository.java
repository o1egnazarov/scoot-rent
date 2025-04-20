package ru.noleg.scootrent.repository;

import ru.noleg.scootrent.entity.tariff.Tariff;

import java.util.List;
import java.util.Optional;

public interface TariffRepository {
    Long save(Tariff tariff);

    void removeById(Long id);

    List<Tariff> getAll();

    Optional<Tariff> findById(Long id);
}
