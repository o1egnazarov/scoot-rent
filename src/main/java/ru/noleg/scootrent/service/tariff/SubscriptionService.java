package ru.noleg.scootrent.service.tariff;

import ru.noleg.scootrent.entity.tariff.UserSubscription;

public interface SubscriptionService {
    void subscribeUser(Long userId, Long tariffId, int minutesUsageLimit);

    UserSubscription getActiveSubscription(Long userId);

    void canselSubscriptionFromUser(Long userId);
}
