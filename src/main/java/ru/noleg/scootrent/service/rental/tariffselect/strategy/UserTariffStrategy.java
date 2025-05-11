package ru.noleg.scootrent.service.rental.tariffselect.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.noleg.scootrent.entity.UserTariff;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.repository.UserTariffRepository;

import java.time.LocalDateTime;

@Component
@Order(2)
public class UserTariffStrategy implements TariffSelectionStrategy {

    private static final Logger logger = LoggerFactory.getLogger(UserTariffStrategy.class);

    private final UserTariffRepository userTariffRepository;

    public UserTariffStrategy(UserTariffRepository userTariffRepository) {
        this.userTariffRepository = userTariffRepository;
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public Tariff selectTariffForUser(Long userId) {
        logger.debug("Выбор специального тарифа для пользователя с id: {}.", userId);
        return userTariffRepository.findActiveTariffByUserAndTime(userId, LocalDateTime.now())
                .map(UserTariff::getTariff)
                .orElse(null);
    }
}
