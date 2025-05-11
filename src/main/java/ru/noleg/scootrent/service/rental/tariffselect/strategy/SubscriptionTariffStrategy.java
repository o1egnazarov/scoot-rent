package ru.noleg.scootrent.service.rental.tariffselect.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.noleg.scootrent.entity.UserSubscription;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.repository.UserSubscriptionRepository;

import java.time.LocalDateTime;

@Component
@Order(1)
public class SubscriptionTariffStrategy implements TariffSelectionStrategy {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionTariffStrategy.class);

    private final UserSubscriptionRepository subscriptionRepository;

    public SubscriptionTariffStrategy(UserSubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public Tariff selectTariffForUser(Long userId) {
        logger.debug("Выбор тарифа по подписке для пользователя с id: {}.", userId);
        return subscriptionRepository.findActiveSubscriptionByUserAndTime(userId, LocalDateTime.now())
                .map(UserSubscription::getTariff)
                .orElse(null);
    }
}
