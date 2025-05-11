package ru.noleg.scootrent.service.rental.tariffselect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.TariffSelectionException;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.service.rental.tariffselect.strategy.TariffSelectionStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TariffSelectionServiceImpl implements TariffSelectionService {

    private static final Logger logger = LoggerFactory.getLogger(TariffSelectionServiceImpl.class);

    private final List<TariffSelectionStrategy> strategies;

    public TariffSelectionServiceImpl(List<TariffSelectionStrategy> strategies) {
        this.strategies = strategies.stream()
                .sorted(Comparator.comparingInt(TariffSelectionStrategy::getPriority))
                .collect(Collectors.toList());
    }

    @Override
    public Tariff selectTariffForUser(Long userId, BillingMode billingMode) {
        logger.info("Поиск подходящего тарифа для пользователя с id: {}.", userId);
        return this.strategies.stream()
                .map(strategy -> {
                    try {
                        return strategy.selectTariff(userId, billingMode);
                    } catch (Exception e) {
                        logger.warn("Ошибка в выборе тарифа {} для пользователя id: {}.",
                                strategy.getClass().getSimpleName(), userId, e);
                        throw new TariffSelectionException("Error on selection tariff.", e);
                    }
                })
                .filter(Objects::nonNull)
                .findFirst()
                .map(tariff -> {
                    logger.info("Выбран тариф {} c типом: {} для пользователя с id: {}.",
                            tariff.getTitle(), tariff.getType(), userId);
                    return tariff;
                })
                .orElseThrow(
                        () -> new BusinessLogicException("No tariff found for user with id: " + userId)
                );
    }
}
