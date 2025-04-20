package ru.noleg.scootrent.repository.impl;

import org.springframework.stereotype.Repository;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.repository.ScooterRepository;
import ru.noleg.scootrent.repository.util.BaseRepositoryImpl;

@Repository
public class ScooterRepositoryJpaImpl extends BaseRepositoryImpl<Scooter, Long> implements ScooterRepository {

    @Override
    protected Class<Scooter> getEntityClass() {
        return Scooter.class;
    }

    @Override
    protected Long getEntityId(Scooter entity) {
        return entity.getId();
    }
}
