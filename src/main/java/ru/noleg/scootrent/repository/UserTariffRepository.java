package ru.noleg.scootrent.repository;

import ru.noleg.scootrent.entity.UserTariff;

import java.util.List;
import java.util.Optional;

public interface UserTariffRepository {
    Long save(UserTariff userTariff);

    void removeById(Long id);

    List<UserTariff> getAll();

    Optional<UserTariff> findById(Long id);
}
