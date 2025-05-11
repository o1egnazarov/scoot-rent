package ru.noleg.scootrent.repository;

import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.repository.util.BaseRepository;

import java.util.Optional;

public interface TariffRepository extends BaseRepository<Tariff, Long> {
    Optional<Tariff> findDefaultTariff();
}
