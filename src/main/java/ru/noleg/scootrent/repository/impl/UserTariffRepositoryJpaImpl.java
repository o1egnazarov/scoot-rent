package ru.noleg.scootrent.repository.impl;

import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.noleg.scootrent.entity.UserTariff;
import ru.noleg.scootrent.exception.RepositoryException;
import ru.noleg.scootrent.repository.UserTariffRepository;
import ru.noleg.scootrent.repository.util.BaseRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UserTariffRepositoryJpaImpl extends BaseRepositoryImpl<UserTariff, Long> implements UserTariffRepository {
    @Override
    protected Class<UserTariff> getEntityClass() {
        return UserTariff.class;
    }

    @Override
    protected Long getEntityId(UserTariff entity) {
        return entity.getId();
    }

    @Override
    public Optional<UserTariff> findActiveTariffByUserAndTime(Long userId, LocalDateTime time) {
        try {

            final String ql = """
                    SELECT ut FROM UserTariff ut
                    WHERE ut.user.id = :userId AND
                    :time BETWEEN ut.validFrom AND ut.validUntil
                    """;

            TypedQuery<UserTariff> query = entityManager.createQuery(ql, UserTariff.class);
            query.setParameter("userId", userId);
            query.setParameter("time", time);

            List<UserTariff> resultList = query.getResultList();
            return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
        } catch (Exception e) {
            throw new RepositoryException("Repository error on fetch userTariff by user and date.", e);
        }
    }
}
