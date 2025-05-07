package ru.noleg.scootrent.repository.impl;

import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.exception.RepositoryException;
import ru.noleg.scootrent.repository.RentalRepository;
import ru.noleg.scootrent.repository.util.BaseRepositoryImpl;

import java.util.List;

@Repository
public class RentalRepositoryJpaImpl extends BaseRepositoryImpl<Rental, Long> implements RentalRepository {
    @Override
    protected Class<Rental> getEntityClass() {
        return Rental.class;
    }

    @Override
    protected Long getEntityId(Rental entity) {
        return entity.getId();
    }

    @Override
    public boolean isActiveRentalByUserId(Long userId) {
        try {

            final String ql = "SELECT COUNT(r) > 0 FROM Rental r WHERE r.user.id = :userId AND r.rentalStatus ='ACTIVE'";

            TypedQuery<Boolean> query = entityManager.createQuery(ql, Boolean.class);
            query.setParameter("userId", userId);

            List<Boolean> resultList = query.getResultList();
            return !resultList.isEmpty() && resultList.get(0);
        } catch (Exception e) {
            throw new RepositoryException("Repository error on fetch user by username", e);
        }
    }

    @Override
    // TODO нужен ли мне вообще этот метод
    public List<Rental> findAllRentals() {
        try {

            final String ql = """
                    SELECT DISTINCT r FROM Rental r
                    LEFT JOIN FETCH r.startPoint
                    LEFT JOIN FETCH r.endPoint
                    LEFT JOIN FETCH r.tariff
                    LEFT JOIN FETCH r.scooter
                    LEFT JOIN FETCH r.scooter.model
                    LEFT JOIN FETCH r.user
                    """;

            TypedQuery<Rental> query = entityManager.createQuery(ql, Rental.class);

            List<Rental> resultList = query.getResultList();
            return resultList.isEmpty() ? List.of() : resultList;
        } catch (Exception e) {
            throw new RepositoryException("Repository error on fetch all rentals.", e);
        }
    }

    @Override
    public List<Rental> findRentalsForUser(Long userId) {
        try {

            final String ql = """
                    SELECT DISTINCT r FROM Rental r
                    LEFT JOIN FETCH r.startPoint
                    LEFT JOIN FETCH r.endPoint
                    LEFT JOIN FETCH r.tariff
                    LEFT JOIN FETCH r.subscription
                    LEFT JOIN FETCH r.scooter
                    LEFT JOIN FETCH r.user
                    WHERE r.user.id = :userId
                    """;

            TypedQuery<Rental> query = entityManager.createQuery(ql, Rental.class);
            query.setParameter("userId", userId);

            List<Rental> resultList = query.getResultList();
            return resultList.isEmpty() ? List.of() : resultList;
        } catch (Exception e) {
            throw new RepositoryException("Repository error on get rental history for user.", e);
        }
    }

    @Override
    public List<Rental> findRentalForScooter(Long scooterId) {
        try {

            final String ql = """
                    SELECT DISTINCT r FROM Rental r
                    LEFT JOIN FETCH r.startPoint
                    LEFT JOIN FETCH r.endPoint
                    LEFT JOIN FETCH r.tariff
                    LEFT JOIN FETCH r.subscription
                    LEFT JOIN FETCH r.scooter
                    LEFT JOIN FETCH r.user
                    WHERE r.scooter.id = :scooterId
                    """;

            TypedQuery<Rental> query = entityManager.createQuery(ql, Rental.class);
            query.setParameter("scooterId", scooterId);

            List<Rental> resultList = query.getResultList();
            return resultList.isEmpty() ? List.of() : resultList;
        } catch (Exception e) {
            throw new RepositoryException("Repository error on get rental history for scooter.", e);
        }
    }
}
