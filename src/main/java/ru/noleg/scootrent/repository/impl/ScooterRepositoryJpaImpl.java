package ru.noleg.scootrent.repository.impl;

import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.exception.RepositoryException;
import ru.noleg.scootrent.repository.ScooterRepository;
import ru.noleg.scootrent.repository.util.BaseRepositoryImpl;

import java.util.List;
import java.util.Optional;

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
