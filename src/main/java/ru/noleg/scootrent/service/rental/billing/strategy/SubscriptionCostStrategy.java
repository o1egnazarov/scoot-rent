package ru.noleg.scootrent.service.rental.billing.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.tariff.UserSubscription;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.CostCalculationException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.UserSubscriptionRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class SubscriptionCostStrategy implements RentalCostStrategy {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionCostStrategy.class);

    private final UserSubscriptionRepository userSubscriptionRepository;

    @Value("${subscription.overused.pricePerMinute}")
    private BigDecimal extraPricePerMinute;

    public SubscriptionCostStrategy(UserSubscriptionRepository userSubscriptionRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    @Override
    public TariffType getSupportedTariffType() {
        return TariffType.SUBSCRIPTION;
    }

    @Override
    @Transactional
    public BigDecimal calculate(User user, Tariff tariff, Duration rentalDuration) {
        logger.debug("Calculate rental cost by subscription for user with id: {}.", user.getId());

        UserSubscription userSubscription = this.getUserSubscription(user);
        logger.debug("Found subscription {}. Total free minutes: {}, used: {}",
                userSubscription.getTariff().getTitle(), userSubscription.getMinuteUsageLimit(), userSubscription.getMinutesUsed());

        long minutesLeft = userSubscription.getMinuteUsageLimit() - userSubscription.getMinutesUsed();
        long rentalMinutes = rentalDuration.toMinutes();
        logger.debug("Minutes left on subscription: {}, minutes requested: {}.", minutesLeft, rentalMinutes);

        this.updateSubscriptionUsage(userSubscription, rentalMinutes);

        BigDecimal totalCost = this.calculateFinalCost(minutesLeft, rentalMinutes, userSubscription);

        logger.debug("Total rental cost for user with id: {} = {}.", user.getId(), totalCost);
        return totalCost;
    }

    private UserSubscription getUserSubscription(User user) {
        return this.userSubscriptionRepository.findActiveSubscriptionByUserAndTime(
                user.getId(), LocalDateTime.now()).orElseThrow(
                () -> {
                    logger.error("Subscription for user with id: {} not found.", user.getId());
                    return new NotFoundException("Subscription for user " + user.getId() + " not found.");
                }
        );
    }

    private void updateSubscriptionUsage(UserSubscription subscription, long minutes) {
        subscription.addMinutes(minutes);
        this.userSubscriptionRepository.save(subscription);
        logger.debug("Updated subscription used: +{} minutes.", minutes);
    }

    private BigDecimal calculateFinalCost(long minutesLeft, long rentalMinutes, UserSubscription userSubscription) {
        try {

            if (minutesLeft >= rentalMinutes) {

                logger.debug("Rental within the subscription.");
                return BigDecimal.ZERO;
            }

            long overused = rentalMinutes - minutesLeft;
            logger.debug("Limit exceeded by {}", overused);

            return BigDecimal.valueOf(overused).multiply(this.extraPricePerMinute);
        } catch (Exception e) {
            logger.error("Error in calculate cost by subscription {}.", userSubscription.getTariff().getTitle(), e);
            throw new CostCalculationException(
                    "Error calculation cost for tariff " + userSubscription.getTariff().getTitle(), e
            );
        }
    }
}
