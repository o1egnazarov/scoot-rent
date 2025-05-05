package ru.noleg.scootrent.repository.impl;

import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.exception.RepositoryException;
import ru.noleg.scootrent.repository.LocationRepository;
import ru.noleg.scootrent.repository.util.BaseRepositoryImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class LocationRepositoryJpaImpl extends BaseRepositoryImpl<LocationNode, Long> implements LocationRepository {
    @Override
    protected Class<LocationNode> getEntityClass() {
        return LocationNode.class;
    }

    @Override
    protected Long getEntityId(LocationNode entity) {
        return entity.getId();
    }

    @Override
    public Optional<LocationNode> findLocationByCoordinates(BigDecimal latitude, BigDecimal longitude) {
        try {
            final String jpql = """
                        SELECT l FROM LocationNode l
                        LEFT JOIN FETCH l.scooters s
                        LEFT JOIN FETCH s.model
                        WHERE l.latitude = :latitude AND l.longitude = :longitude
                    """;


            TypedQuery<LocationNode> query = this.entityManager.createQuery(jpql, LocationNode.class);
            query.setParameter("latitude", latitude);
            query.setParameter("longitude", longitude);

            List<LocationNode> resultList = query.getResultList();
            return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
        } catch (Exception e) {
            throw new RepositoryException("Repository error on fetch location by coordinates.", e);
        }
    }

    @Override
    public List<LocationNode> findAll() {
        try {
            final String jpql = """
                    SELECT l FROM LocationNode l LEFT JOIN FETCH l.children
                    """;

            TypedQuery<LocationNode> query = this.entityManager.createQuery(jpql, LocationNode.class);

            List<LocationNode> resultList = query.getResultList();
            return resultList.isEmpty() ? List.of() : resultList;
        } catch (Exception e) {
            throw new RepositoryException("Repository error on fetch all locations.", e);
        }
    }

    @Override
    public Optional<LocationNode> findLocationById(Long id) {
        try {
            final String jpql = """
                    SELECT l FROM LocationNode l
                    LEFT JOIN FETCH l.scooters s
                    LEFT JOIN FETCH s.model m
                    WHERE l.id = :id
                    """;

            TypedQuery<LocationNode> query = this.entityManager.createQuery(jpql, LocationNode.class);
            query.setParameter("id", id);

            List<LocationNode> resultList = query.getResultList();
            return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
        } catch (Exception e) {
            throw new RepositoryException("Repository error on fetch location by id.", e);
        }
    }

    @Override
    public List<LocationNode> findChildrenLocationByParentId(Long parentId) {
        try {
            final String jpql = """
                    SELECT l FROM LocationNode l
                    LEFT JOIN FETCH l.scooters s
                    LEFT JOIN FETCH s.model m
                    WHERE l.parent.id = :parentId
                    """;

            TypedQuery<LocationNode> query = this.entityManager.createQuery(jpql, LocationNode.class);
            query.setParameter("parentId", parentId);

            List<LocationNode> resultList = query.getResultList();
            return resultList.isEmpty() ? List.of() : resultList;
        } catch (Exception e) {
            throw new RepositoryException("Repository error on fetch all locations.", e);
        }
    }
}
