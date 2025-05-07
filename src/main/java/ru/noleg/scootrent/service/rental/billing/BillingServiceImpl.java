package ru.noleg.scootrent.service.rental.billing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.UserSubscription;
import ru.noleg.scootrent.entity.UserTariff;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.repository.UserSubscriptionRepository;
import ru.noleg.scootrent.repository.UserTariffRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class BillingServiceImpl implements BillingService {

    private static final Logger logger = LoggerFactory.getLogger(BillingServiceImpl.class);

    private final UserTariffRepository userTariffRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    @Value("${subscription.overused.pricePerMinute}")
    private BigDecimal extraPricePerMinute;


    public BillingServiceImpl(UserTariffRepository userTariffRepository,
                              UserSubscriptionRepository userSubscriptionRepository) {
        this.userTariffRepository = userTariffRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    @Override
    @Transactional
    public BigDecimal calculateRentalCost(User user, Duration rentalDuration) {
        try {
            logger.info("Расчет цены за аренду для пользователя c id {}.", user.getId());

            UserSubscription userSubscription =
                    this.userSubscriptionRepository.findActiveSubscriptionByUserAndTime(user.getId(), LocalDateTime.now()).orElse(null);

            if (userSubscription != null && !isExpired(userSubscription)) {
                logger.info("Расчет стоимости поездки с учетом подписки с id: {}.", userSubscription.getId());
                return this.calculateSubCost(userSubscription, rentalDuration);
            }

            UserTariff userTariff =
                    this.userTariffRepository.findActiveTariffByUserAndTime(user.getId(), LocalDateTime.now()).orElse(null);

            if (userTariff != null) {
                logger.info("Расчет стоимости поездки с учетом тарифа с id: {}.", userTariff.getId());
                return this.calculateTariffCost(userTariff, rentalDuration);
            }

            logger.error("Тариф или подписка для пользователя с id {} не найдены.", user.getId());
            throw new BusinessLogicException("No tariff and subscription found for user " + user.getId());
        } catch (BusinessLogicException e) {

            logger.error("Исключение бизнес-логики при расчете стоимости аренды.", e);
            throw e;
        } catch (Exception e) {

            logger.error("Исключение в сервисе при расчете стоимости аренды.", e);
            throw new ServiceException("Error on calculate rental cost", e);
        }
    }

    private boolean isExpired(UserSubscription userSubscription) {
        return userSubscription.getEndDate().isBefore(LocalDateTime.now());
    }

    private BigDecimal calculateSubCost(UserSubscription userSubscription, Duration rentalDuration) {

        logger.info("Calculate cost for subscription: {} with rental duration: {}.", userSubscription.getId(), rentalDuration);

        long minutesLeft = userSubscription.getMinuteUsageLimit() - userSubscription.getMinutesUsed();
        long rentalMinutes = rentalDuration.toMinutes();

        logger.debug("Rental took: {} minute. Minutes left: {}.", rentalMinutes, minutesLeft);

        if (minutesLeft >= rentalMinutes) {

            userSubscription.addMinutes(rentalMinutes);
            this.userSubscriptionRepository.save(userSubscription);
            logger.info("Rental was: {}.", BigDecimal.ZERO);

            return BigDecimal.ZERO;
        } else {

            long overused = rentalMinutes - minutesLeft;
            logger.debug("Rental exceeded the limit by {} minutes.", overused);
            userSubscription.addMinutes(rentalMinutes);

            this.userSubscriptionRepository.save(userSubscription);

            // TODO цена за лишние минуты (дорабоать)
            BigDecimal result = BigDecimal.valueOf(overused).multiply(this.extraPricePerMinute);
            logger.info("Rental with overused minutes was: {}.", result);
            return result;
        }
    }

    private BigDecimal calculateTariffCost(UserTariff userTariff, Duration rentalDuration) {

        logger.info("Calculate cost for tariff: {} with rental duration: {}.", userTariff.getId(), rentalDuration);

        // выбор кастомной цены для пользователя или базовая цена
        BigDecimal price = userTariff.getCustomPricePerMinute() != null ?
                userTariff.getCustomPricePerMinute() :
                userTariff.getTariff().getPricePerMinute();

        logger.debug("Price per minute: {}.", price);

        // если есть скидка (указывается в процентах) применяем
        if (userTariff.getDiscountPct() != null) {
            BigDecimal discount = new BigDecimal(userTariff.getDiscountPct()).divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_DOWN);
            price = price.subtract(price.multiply(discount));
            logger.debug("Price per minute: {}, with discount pct: {}.", price, discount);
        }

        // на данном этапе получили цену за unit
        // теперь надо посчитать количество таких unit, использованных за поездку
        // все перемножить

        BigDecimal rentalMinutes = BigDecimal.valueOf(rentalDuration.toMinutes());
        price = price.multiply(rentalMinutes);

        BigDecimal result = price.add(new BigDecimal(userTariff.getTariff().getUnlockFee()));

        logger.info("Result price with unlock fee (optional): {}.", result);
        return result;
    }
}
