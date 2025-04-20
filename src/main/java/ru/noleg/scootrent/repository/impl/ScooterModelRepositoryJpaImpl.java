package ru.noleg.scootrent.repository.impl;

import org.springframework.stereotype.Repository;
import ru.noleg.scootrent.entity.scooter.ScooterModel;
import ru.noleg.scootrent.repository.ScooterModelRepository;
import ru.noleg.scootrent.repository.util.BaseRepositoryImpl;

@Repository
public class ScooterModelRepositoryJpaImpl extends BaseRepositoryImpl<ScooterModel, Long> implements ScooterModelRepository {

    @Override
    protected Class<ScooterModel> getEntityClass() {
        return ScooterModel.class;
    }

    @Override
    protected Long getEntityId(ScooterModel entity) {
        return entity.getId();
    }
}
