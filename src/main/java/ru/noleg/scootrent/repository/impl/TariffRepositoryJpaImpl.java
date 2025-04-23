package ru.noleg.scootrent.repository.impl;

import org.springframework.stereotype.Repository;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.repository.TariffRepository;
import ru.noleg.scootrent.repository.util.BaseRepositoryImpl;

@Repository
public class TariffRepositoryJpaImpl extends BaseRepositoryImpl<Tariff, Long> implements TariffRepository {
    @Override
    protected Class<Tariff> getEntityClass() {
        return Tariff.class;
    }

    @Override
    protected Long getEntityId(Tariff entity) {
        return entity.getId();
    }
}
