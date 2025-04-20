package ru.noleg.scootrent.repository;

import ru.noleg.scootrent.entity.UserSubscription;

import java.util.List;
import java.util.Optional;

public interface UserSubscriptionRepository {
    Long save(UserSubscription userSubscription);

    void removeById(Long id);

    List<UserSubscription> getAll();

    Optional<UserSubscription> findById(Long id);
}
