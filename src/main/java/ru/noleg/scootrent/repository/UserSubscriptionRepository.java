package ru.noleg.scootrent.repository;

import ru.noleg.scootrent.entity.UserSubscription;
import ru.noleg.scootrent.repository.util.BaseRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserSubscriptionRepository extends BaseRepository<UserSubscription, Long> {
    Optional<UserSubscription> findActiveSubscriptionByUserAndTime(Long userId, LocalDateTime time);
}
