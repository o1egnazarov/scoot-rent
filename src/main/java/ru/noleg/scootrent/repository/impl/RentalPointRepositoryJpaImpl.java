package ru.noleg.scootrent.repository.impl;

import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.noleg.scootrent.entity.rental.RentalPoint;
import ru.noleg.scootrent.exception.RepositoryException;
import ru.noleg.scootrent.repository.RentalPointRepository;
import ru.noleg.scootrent.repository.util.BaseRepositoryImpl;

import java.util.List;

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


    @Override
    public List<RentalPoint> findAllRentalPointByDistrict(Long countryId, Long cityId, Long districtId) {
        try {

            final String jpql = """
                        SELECT rp FROM RentalPoint rp
                        WHERE rp.parent.id = :districtId
                        AND rp.parent.parent.id = :cityId
                        AND rp.parent.parent.parent.id = :countryId
                    """;

            TypedQuery<RentalPoint> query = entityManager.createQuery(jpql, RentalPoint.class);
            query.setParameter("districtId", districtId);
            query.setParameter("cityId", cityId);
            query.setParameter("countryId", countryId);

            List<RentalPoint> resultList = query.getResultList();
            return resultList == null ? List.of() : resultList;
        } catch (Exception e) {
            throw new RepositoryException("Repository error on fetch rental points by district.", e);
        }
    }
}
