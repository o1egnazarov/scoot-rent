package ru.noleg.scootrent.service.rental.billing.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.entity.tariff.UserTariff;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.CostCalculationException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.UserTariffRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class UserTariffCostStrategy implements RentalCostStrategy {

    private static final Logger logger = LoggerFactory.getLogger(UserTariffCostStrategy.class);

    private final UserTariffRepository userTariffRepository;

    public UserTariffCostStrategy(UserTariffRepository userTariffRepository) {
        this.userTariffRepository = userTariffRepository;
    }

    @Override
    public TariffType getSupportedTariffType() {
        return TariffType.SPECIAL_TARIFF;
    }

    @Override
    @Transactional
    public BigDecimal calculate(User user, Tariff tariff, Duration rentalDuration) {
        logger.debug("Calculate cost for rental by special tariff for user with id: {}.", user.getId());

        UserTariff userTariff = this.getUserTariff(user);
        logger.debug("Found special tariff {}. Price per minute: {}, discount: {}.",
                userTariff.getTariff().getTitle(), userTariff.getCustomPricePerMinute(), userTariff.getDiscountPct());

        try {

            BigDecimal price = this.calculatePricePerMinute(userTariff);

            BigDecimal totalCost = calculateFinalCost(rentalDuration, price, userTariff);

            logger.debug("Total rental cost for user with id: {} = {}.", user.getId(), totalCost);
            return totalCost;
        } catch (Exception e) {
            logger.error("Error in calculate cost by tariff {}.", userTariff.getTariff().getTitle(), e);
            throw new CostCalculationException(
                    String.format("Error calculation cost for user with id: %d by tariff %s.",
                            user.getId(), userTariff.getTariff().getTitle()), e
            );
        }
    }

    private UserTariff getUserTariff(User user) {
        return this.userTariffRepository.findActiveTariffByUserAndTime(user.getId(), LocalDateTime.now()).orElseThrow(
                () -> {
                    logger.error("Special tariff for user with id: {} not found.", user.getId());
                    return new NotFoundException("Special tariff for user " + user.getId() + " not found.");
                }
        );
    }

    private BigDecimal calculatePricePerMinute(UserTariff userTariff) {
        BigDecimal price = Optional.ofNullable(userTariff.getCustomPricePerMinute())
                .orElse(userTariff.getTariff().getPricePerUnit());

        if (userTariff.getDiscountPct() != null) {
            price = this.calculateDiscount(userTariff, price);
        }
        return price;
    }

    private BigDecimal calculateDiscount(UserTariff userTariff, BigDecimal price) {
        BigDecimal discount = new BigDecimal(userTariff.getDiscountPct())
                .divide(BigDecimal.valueOf(100), 1, RoundingMode.HALF_DOWN);

        price = price.subtract(price.multiply(discount));

        logger.debug("Price per minute: {}.", price);
        return price;
    }

    private BigDecimal calculateFinalCost(Duration rentalDuration, BigDecimal price, UserTariff userTariff) {
        BigDecimal rentalMinutes = BigDecimal.valueOf(rentalDuration.toMinutes());
        logger.debug("Duration rental: {}.", rentalMinutes);

        price = price.multiply(rentalMinutes);
        logger.debug("Rental cost (without unlock fee): {}", price);

        return price.add(new BigDecimal(userTariff.getTariff().getUnlockFee()));
    }
}
