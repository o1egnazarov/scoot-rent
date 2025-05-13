package ru.noleg.scootrent.service.rental.billing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.service.rental.billing.strategy.RentalCostStrategy;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BillingServiceImpl implements BillingService {

    private static final Logger logger = LoggerFactory.getLogger(BillingServiceImpl.class);

    private final Map<TariffType, RentalCostStrategy> strategyMap;

    public BillingServiceImpl(List<RentalCostStrategy> strategies) {
        this.strategyMap = initializeMap(strategies);
    }

    private Map<TariffType, RentalCostStrategy> initializeMap(List<RentalCostStrategy> strategies) {
        Map<TariffType, RentalCostStrategy> map = new EnumMap<>(TariffType.class);

        strategies.forEach(strategy -> {
            TariffType type = strategy.getSupportedTariffType();
            if (map.containsKey(type)) {
                throw new BusinessLogicException("Duplicate strategy for tariff type: " + type);
            }
            map.put(type, strategy);
            logger.debug("Strategy added {} for tariff with type: {}.",
                    strategy.getClass().getSimpleName(), type);
        });
        return map;
    }

    @Override
    public BigDecimal calculateRentalCost(User user, Tariff tariff, Duration rentalDuration) {
        TariffType type = tariff.getType();
        logger.debug("Calculation of the cost for tariff type: {}.", type);

        RentalCostStrategy strategy = Optional.ofNullable(this.strategyMap.get(type)).orElseThrow(
                () -> {
                    logger.error("Tariff with type: {} unsupported.", type);
                    return new BusinessLogicException("Unsupported tariff type: " + type);
                }
        );

        return strategy.calculate(user, tariff, rentalDuration);
    }
}
