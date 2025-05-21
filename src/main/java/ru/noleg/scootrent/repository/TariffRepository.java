package ru.noleg.scootrent.repository;

import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.repository.util.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface TariffRepository extends BaseRepository<Tariff, Long> {
    Optional<Tariff> findDefaultTariffByBillingMode(BillingMode billingMode);

    List<Tariff> findByActiveTrue();
}
