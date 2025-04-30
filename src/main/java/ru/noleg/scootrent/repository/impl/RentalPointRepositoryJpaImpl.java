package ru.noleg.scootrent.repository.impl;

import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.noleg.scootrent.entity.rental.RentalPoint;
import ru.noleg.scootrent.exception.RepositoryException;
import ru.noleg.scootrent.repository.RentalPointRepository;
import ru.noleg.scootrent.repository.util.BaseRepositoryImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<RentalPoint> findRentalPointByCoordinates(BigDecimal latitude, BigDecimal longitude) {
        try {

            final String jpql = """
                        SELECT DISTINCT rp FROM RentalPoint rp
                        LEFT JOIN FETCH rp.parent
                        LEFT JOIN FETCH rp.children
                        LEFT JOIN FETCH rp.scooters
                        WHERE rp.latitude = :latitude AND rp.longitude = :longitude
                    """;


            TypedQuery<RentalPoint> query = entityManager.createQuery(jpql, RentalPoint.class);
            query.setParameter("latitude", latitude);
            query.setParameter("longitude", longitude);

            List<RentalPoint> resultList = query.getResultList();
            return resultList == null ? Optional.empty() : Optional.of(resultList.get(0));
        } catch (Exception e) {
            throw new RepositoryException("Repository error on fetch rental points by district.", e);
        }
    }

    @Override
    public List<RentalPoint> findAllRentalPoints() {
        try {
            final String jpql = """
                        SELECT DISTINCT rp FROM RentalPoint rp
                        LEFT JOIN FETCH rp.children
                        WHERE rp.parent IS NULL
                    """;

            TypedQuery<RentalPoint> query = entityManager.createQuery(jpql, RentalPoint.class);

            List<RentalPoint> resultList = query.getResultList();
            return resultList == null ? List.of() : resultList;
        } catch (Exception e) {
            throw new RepositoryException("Repository error on fetch rental points by district.", e);
        }
    }

    @Override
    public Optional<RentalPoint> findRentalPointById(Long id) {
        try {
            final String jpql = """
                        SELECT DISTINCT rp FROM RentalPoint rp
                        LEFT JOIN FETCH rp.children
                        LEFT JOIN FETCH rp.scooters
                        WHERE rp.id = :id
                    """;

            TypedQuery<RentalPoint> query = entityManager.createQuery(jpql, RentalPoint.class);
            query.setParameter("id", id);

            List<RentalPoint> resultList = query.getResultList();
            return resultList == null ? Optional.empty() : Optional.of(resultList.get(0));
        } catch (Exception e) {
            throw new RepositoryException("Repository error on fetch rental points by district.", e);
        }
    }
}
