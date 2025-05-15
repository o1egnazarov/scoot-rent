package ru.noleg.scootrent.service.tariff;

import jakarta.validation.constraints.Min;
import ru.noleg.scootrent.entity.UserSubscription;

public interface SubscriptionService {
    void subscribeUser(Long userId, Long tariffId, int minutesUsageLimit);

    UserSubscription getActiveSubscription(Long userId);

    void canselSubscriptionFromUser(Long userId);
}
