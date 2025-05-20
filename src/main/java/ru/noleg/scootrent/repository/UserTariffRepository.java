package ru.noleg.scootrent.repository;

import ru.noleg.scootrent.entity.tariff.UserTariff;
import ru.noleg.scootrent.repository.util.BaseRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserTariffRepository extends BaseRepository<UserTariff, Long> {
    Optional<UserTariff> findActiveTariffByUserAndTime(Long userId, LocalDateTime time);
}
