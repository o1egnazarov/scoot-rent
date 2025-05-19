package ru.noleg.scootrent.service.rental.billing.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
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
    private static final BigDecimal SHORT_RIDE_THRESHOLD_MINUTES = BigDecimal.valueOf(10);
    private static final BigDecimal LONG_RIDE_THRESHOLD_MINUTES = BigDecimal.valueOf(30);
    private static final BigDecimal SHORT_RIDE_SURCHARGE = BigDecimal.valueOf(1.2);
    private static final BigDecimal LONG_RIDE_DISCOUNT = BigDecimal.valueOf(0.8);

    @Override
    public TariffType getSupportedTariffType() {
        return TariffType.DEFAULT_TARIFF;
    }

    @Override
    public BigDecimal calculate(User user, Tariff tariff, Duration rentalDuration) {
        logger.debug("Calculate rental cost by default tariff: {} for user with id: {}. ", tariff.getTitle(), user.getId());

        BigDecimal totalCost = this.calculateFinalCost(user, rentalDuration, tariff);

        logger.debug("Total rental cost for user with id: {} = {}.", user.getId(), totalCost);
        return totalCost;
    }

    private BigDecimal calculateFinalCost(User user, Duration rentalDuration, Tariff tariff) {
        try {
            BigDecimal cost = tariff.getPricePerUnit();
            BigDecimal minutes = BigDecimal.valueOf(rentalDuration.toMinutes());
            BigDecimal unlockFee = BigDecimal.valueOf(tariff.getUnlockFee());
            logger.debug("Price per unit: {}, duration: {}, unlock fee: {}.", cost, rentalDuration, unlockFee);

            switch (tariff.getBillingMode()) {
                case PER_MINUTE -> cost = cost.multiply(minutes);
                case PER_HOUR -> {

                    cost = cost.multiply(minutes.divide(MINUTES_IN_HOUR, 2, RoundingMode.HALF_UP));

                    if (minutes.compareTo(SHORT_RIDE_THRESHOLD_MINUTES) < 0) {
                        cost = cost.multiply(SHORT_RIDE_SURCHARGE);
                        logger.debug("Short ride detected (<10 min). Applying surcharge: x{}.", SHORT_RIDE_SURCHARGE);
                    }

                    if (minutes.compareTo(LONG_RIDE_THRESHOLD_MINUTES) > 0) {
                        cost = cost.multiply(LONG_RIDE_DISCOUNT);
                        logger.debug("Long ride detected (>30 min). Applying discount: x{}.", LONG_RIDE_DISCOUNT);
                    }
                }
                default -> throw new IllegalArgumentException("Unsupported billing mode: " + tariff.getBillingMode());
            }

            return cost.add(unlockFee);
        } catch (Exception e) {
            logger.error("Error in calculate cost by default tariff: {}.", tariff.getTitle(), e);
            throw new CostCalculationException(
                    String.format("Error calculation cost for user with id: %d by tariff %s.",
                            user.getId(), tariff.getTitle()), e);
        }
    }
}
