package ru.noleg.scootrent.service.rental.tariffselect.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.TariffRepository;

@Component
public class DefaultTariffStrategy implements TariffSelectionStrategy {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTariffStrategy.class);

    private final TariffRepository tariffRepository;

    public DefaultTariffStrategy(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public Tariff selectTariffForUser(Long userId) {
        logger.debug("Выбор тарифа по умолчанию для пользователя с id: {}.", userId);
        return tariffRepository.findDefaultTariff().orElseThrow(
                () -> {
                    logger.error("Для пользователя с id: {} не найден тариф по умолчанию.", userId);
                    return new NotFoundException("Default tariff not found.");
                }
        );
    }
}
