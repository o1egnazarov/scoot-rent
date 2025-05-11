package ru.noleg.scootrent.service.rental.billing.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.CostCalculationException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.TariffRepository;

import java.math.BigDecimal;
import java.time.Duration;

@Component
public class DefaultTariffCostStrategy implements RentalCostStrategy {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTariffCostStrategy.class);

    private final TariffRepository tariffRepository;

    public DefaultTariffCostStrategy(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    public TariffType getSupportedTariffType() {
        return TariffType.DEFAULT_TARIFF;
    }

    @Override
    public BigDecimal calculate(User user, Duration rentalDuration) {
        logger.info("Расчет стоимости аренды по умолчанию для пользователя с id: {}.", user.getId());
        logger.debug("Длительность аренды {}.", rentalDuration);

        Tariff tariff = this.getDefaultTariff();
        logger.debug("Найден тариф по умолчанию {}. Цена за минуту: {}, цена за старт: {}",
                tariff.getTitle(), tariff.getPricePerMinute(), tariff.getUnlockFee());

        BigDecimal totalCost = this.calculateFinalCost(user, rentalDuration, tariff);

        logger.info("Итоговая стоимость аренды для пользователя с id: {} = {}", user.getId(), totalCost);
        return totalCost;
    }

    private Tariff getDefaultTariff() {
        return this.tariffRepository.findDefaultTariff().orElseThrow(
                () -> {
                    logger.error("Тариф по умолчанию не найден.");
                    return new NotFoundException("Default tariff not found.");
                }
        );
    }

    private BigDecimal calculateFinalCost(User user, Duration rentalDuration, Tariff tariff) {
        try {

            BigDecimal pricePerMinute = tariff.getPricePerMinute();
            BigDecimal rentalMinutes = BigDecimal.valueOf(rentalDuration.toMinutes());
            BigDecimal unlockFee = BigDecimal.valueOf(tariff.getUnlockFee());

            return pricePerMinute
                    .multiply(rentalMinutes)
                    .add(unlockFee);
        } catch (Exception e) {
            logger.error("Ошибка в расчете стоимости по тарифу {}", tariff.getTitle(), e);
            throw new CostCalculationException(
                    String.format("Error calculation cost for user with id: %d by tariff %s", user.getId(), tariff.getTitle()), e
            );
        }
    }
}
