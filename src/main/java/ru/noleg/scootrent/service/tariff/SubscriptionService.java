package ru.noleg.scootrent.service.tariff;

public interface SubscriptionService {
    void subscribeUser(Long userId, Long tariffId, Integer minutesUsageLimit);
}
