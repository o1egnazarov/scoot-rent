package ru.noleg.scootrent.repository.impl;

import org.springframework.stereotype.Repository;
import ru.noleg.scootrent.entity.rental.RentalPoint;
import ru.noleg.scootrent.repository.RentalPointRepository;
import ru.noleg.scootrent.repository.util.BaseRepositoryImpl;

@Repository
public class RentalPointRepositoryJpaImpl extends BaseRepositoryImpl<RentalPoint, Long> implements RentalPointRepository {
    @Override
    protected Class<RentalPoint> getEntityClass() {
        return RentalPoint.class;
    }

    @Override
    protected Long getEntityId(RentalPoint entity) {
        return entity.getId();
    }


}
