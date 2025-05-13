package ru.noleg.scootrent.repository.impl;

import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.noleg.scootrent.entity.UserSubscription;
import ru.noleg.scootrent.exception.RepositoryException;
import ru.noleg.scootrent.repository.UserSubscriptionRepository;
import ru.noleg.scootrent.repository.util.BaseRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UserSubscriptionRepositoryJpaImpl extends BaseRepositoryImpl<UserSubscription, Long> implements UserSubscriptionRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserSubscriptionRepositoryJpaImpl.class);

    @Override
    protected Class<UserSubscription> getEntityClass() {
        return UserSubscription.class;
    }

    @Override
    protected Long getEntityId(UserSubscription entity) {
        return entity.getId();
    }

    @Override
    public Optional<UserSubscription> findActiveSubscriptionByUserAndTime(Long userId, LocalDateTime time) {
        try {

            final String ql = """
                    SELECT us FROM UserSubscription us
                    WHERE us.user.id = :userId AND
                    :time BETWEEN us.startDate AND us.endDate
                    """;

            TypedQuery<UserSubscription> query = entityManager.createQuery(ql, UserSubscription.class);
            query.setParameter("userId", userId);
            query.setParameter("time", time);

            List<UserSubscription> resultList = query.getResultList();
            return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
        } catch (Exception e) {
            logger.error("Failed to find active subscription for user with id: {} and by time: {}.", userId, time, e);
            throw new RepositoryException("Repository error on fetch subscription by user and date.", e);
        }
    }
}
