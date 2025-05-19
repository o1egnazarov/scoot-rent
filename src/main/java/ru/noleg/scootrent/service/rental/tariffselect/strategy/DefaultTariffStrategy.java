package ru.noleg.scootrent.service.rental.tariffselect.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.TariffRepository;

@Component
public class DefaultTariffStrategy implements TariffSelectionStrategy {

    private static final int LOWEST_PRIORITY = 100;
    private static final Logger logger = LoggerFactory.getLogger(DefaultTariffStrategy.class);

    private final TariffRepository tariffRepository;

    public DefaultTariffStrategy(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    public int getPriority() {
        return LOWEST_PRIORITY;
    }

    @Override
    public Tariff selectTariff(Long userId, BillingMode billingMode) {
        logger.debug("Selecting default tariff for user with id: {}, by: {}.", userId, billingMode);
        return tariffRepository.findDefaultTariffByBillingMode(billingMode).orElseThrow(
                () -> {
                    logger.error("For user with id: {} not found default tariff.", userId);
                    return new NotFoundException("Default tariff not found.");
                }
        );
    }
}
