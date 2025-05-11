package ru.noleg.scootrent.service.rental.billing.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.CostCalculationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

@Component
public class DefaultTariffCostStrategy implements RentalCostStrategy {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTariffCostStrategy.class);

    private static final BigDecimal MINUTES_IN_HOUR = BigDecimal.valueOf(60);

    @Override
    public TariffType getSupportedTariffType() {
        return TariffType.DEFAULT_TARIFF;
    }

    @Override
    public BigDecimal calculate(User user, Tariff tariff, Duration rentalDuration) {
        logger.debug("Расчет стоимости аренды по умолчанию для пользователя с id: {}. ", user.getId());

        BigDecimal totalCost = this.calculateFinalCost(user, rentalDuration, tariff);

        logger.debug("Итоговая стоимость аренды для пользователя с id: {} = {}", user.getId(), totalCost);
        return totalCost;
    }

    private BigDecimal calculateFinalCost(User user, Duration rentalDuration, Tariff tariff) {
        try {
            BigDecimal cost = tariff.getPricePerUnit();
            BigDecimal minutes = BigDecimal.valueOf(rentalDuration.toMinutes());
            BigDecimal unlockFee = BigDecimal.valueOf(tariff.getUnlockFee());

            if (tariff.getBillingMode() == BillingMode.PER_HOUR) {
                cost = cost
                        .multiply(MINUTES_IN_HOUR)
                        .multiply(minutes.divide(MINUTES_IN_HOUR, 2, RoundingMode.HALF_UP));
            } else {
                cost = cost.multiply(minutes);
            }

            return cost.add(unlockFee);
        } catch (Exception e) {
            logger.error("Ошибка в расчете стоимости по тарифу {}", tariff.getTitle(), e);
            throw new CostCalculationException(
                    String.format("Error calculation cost for user with id: %d by tariff %s", user.getId(), tariff.getTitle()), e
            );
        }
    }
}
