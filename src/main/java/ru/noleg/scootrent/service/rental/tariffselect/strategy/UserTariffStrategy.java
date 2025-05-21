package ru.noleg.scootrent.service.rental.tariffselect.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.UserTariff;
import ru.noleg.scootrent.repository.UserTariffRepository;

import java.time.LocalDateTime;

@Component
public class UserTariffStrategy implements TariffSelectionStrategy {

    private static final int PRIORITY = 2;
    private static final Logger logger = LoggerFactory.getLogger(UserTariffStrategy.class);

    private final UserTariffRepository userTariffRepository;

    public UserTariffStrategy(UserTariffRepository userTariffRepository) {
        this.userTariffRepository = userTariffRepository;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public Tariff selectTariff(Long userId, BillingMode billingMode) {
        logger.debug("Selecting special tariff for user with id: {}.", userId);
        return userTariffRepository.findActiveTariffByUserAndTime(userId, LocalDateTime.now())
                .map(UserTariff::getTariff)
                .orElse(null);
    }
}
