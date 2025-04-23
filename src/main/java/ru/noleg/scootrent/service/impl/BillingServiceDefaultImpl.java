package ru.noleg.scootrent.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.UserSubscription;
import ru.noleg.scootrent.entity.UserTariff;
import ru.noleg.scootrent.entity.tariff.DurationType;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.repository.UserSubscriptionRepository;
import ru.noleg.scootrent.repository.UserTariffRepository;
import ru.noleg.scootrent.service.BillingService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class BillingServiceDefaultImpl implements BillingService {

    private final UserTariffRepository userTariffRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;


    public BillingServiceDefaultImpl(UserTariffRepository userTariffRepository,
                                     UserSubscriptionRepository userSubscriptionRepository) {
        this.userTariffRepository = userTariffRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    @Override
    @Transactional
    public BigDecimal calculateRentalCost(User user, Duration rentalDuration) {
        try {

            UserSubscription userSubscription =
                    this.userSubscriptionRepository.findActiveSubscriptionByUserAndTime(user.getId(), LocalDateTime.now()).orElse(null);

            if (userSubscription != null && !isExpired(userSubscription)) {
                return this.calculateSubCost(userSubscription, rentalDuration);
            }

            UserTariff userTariff =
                    this.userTariffRepository.findActiveTariffByUserAndTime(user.getId(), LocalDateTime.now()).orElse(null);

            if (userTariff != null) {
                return this.calculateTariffCost(userTariff, rentalDuration);
            }

            throw new BusinessLogicException("No tariff and subscription found for user " + user.getId());
        } catch (Exception e) {

            throw new ServiceException("Error on calculate rental cost", e);
        }
    }

    private boolean isExpired(UserSubscription userSubscription) {
        return userSubscription.getEndDate().isBefore(LocalDateTime.now());
    }

    private BigDecimal calculateSubCost(UserSubscription userSubscription, Duration rentalDuration) {

        long minutesLeft = userSubscription.getMinuteUsageLimit() - userSubscription.getMinutesUsed();
        long rentalMinutes = rentalDuration.toMinutes();

        if (minutesLeft >= rentalMinutes) {

            userSubscription.addMinutes(rentalMinutes);
            this.userSubscriptionRepository.save(userSubscription);
            return BigDecimal.ZERO;
        } else {

            int overused = (int) (rentalMinutes - minutesLeft);
            userSubscription.addMinutes(rentalMinutes);
            this.userSubscriptionRepository.save(userSubscription);
            // TODO цена за лишнюю минуту (дорабоать)
            return new BigDecimal(overused).multiply(new BigDecimal("1.01"));
        }
    }

    private BigDecimal calculateTariffCost(UserTariff userTariff, Duration rentalDuration) {

        // выбор кастомной цены для пользователя или базовая цена
        BigDecimal price = userTariff.getCustomPricePerUnit() != null ?
                userTariff.getCustomPricePerUnit() :
                userTariff.getTariff().getPricePerUnit();


        // если есть скидка (указывается в процентах) применяем
        if (userTariff.getDiscountPct() != null) {
            BigDecimal discount = new BigDecimal(userTariff.getDiscountPct()).divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_DOWN);
            price = price.multiply(discount);
        }

        // на данном этапе получили цену за unit
        // теперь надо посчитать количество таких unit, использованных за поездку
        // все перемножить

        long usedUnits = this.getUsedUnits(userTariff, rentalDuration);

        BigDecimal result = price.multiply(new BigDecimal(usedUnits));

        // плюс цена за старт (если есть)
        return result.add(new BigDecimal(userTariff.getTariff().getUnlockFee()));
    }

    // TODO додумать надо
    private long getUsedUnits(UserTariff userTariff, Duration rentalDuration) {
        int unitValue = userTariff.getTariff().getDurationValue();


        System.out.println("used unit: " + (long) Math.ceil((double) rentalDuration.toDays() / unitValue));

        DurationType unit = userTariff.getTariff().getDurationUnit();
        return switch (unit) {
//            case MINUTES -> (long) Math.ceil((double) rentalDuration.toMinutes() / unitValue);
            case HOUR -> (long) Math.ceil((double) rentalDuration.toHours() / unitValue);
//            case DAY -> {
//                long minutes = rentalDuration.toMinutes();
//                BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 3, RoundingMode.HALF_DOWN);
//
//                long ceil = (long) Math.ceil(((double) rentalDuration.toDays()) / unitValue);
//            }
            case DAY -> (long) Math.ceil((double) rentalDuration.toDays() / unitValue);
            case WEEK -> (long) Math.ceil((double) (rentalDuration.toDays() * 7) / unitValue);
            case MONTH -> (long) Math.ceil((double) (rentalDuration.toDays() * 30) / unitValue);
            case YEAR -> (long) Math.ceil((double) (rentalDuration.toDays() * 365) / unitValue);
        };
    }
}
