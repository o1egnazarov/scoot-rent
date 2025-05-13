package ru.noleg.scootrent.service.tariff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.UserSubscription;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.UserNotFoundException;
import ru.noleg.scootrent.repository.TariffRepository;
import ru.noleg.scootrent.repository.UserRepository;
import ru.noleg.scootrent.repository.UserSubscriptionRepository;

import java.time.LocalDateTime;

@Service
@Transactional
public class SubscriptionAssignmentServiceImpl implements SubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionAssignmentServiceImpl.class);

    private final UserRepository userRepository;
    private final TariffRepository tariffRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    public SubscriptionAssignmentServiceImpl(UserRepository userRepository,
                                             TariffRepository tariffRepository,
                                             UserSubscriptionRepository userSubscriptionRepository) {
        this.userRepository = userRepository;
        this.tariffRepository = tariffRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    @Override
    public void subscribeUser(Long userId, Long tariffId, Integer minutesUsageLimit) {
        logger.debug("Assign subscription with limit = {}.", minutesUsageLimit);

        User user = this.getUser(userId);
        Tariff tariff = this.validateAndGetTariff(tariffId);

        UserSubscription userSubscription = this.createUserSubscription(minutesUsageLimit, tariff, user);
        logger.debug("Subscription {} successfully assigned to user with id: {}.", tariffId, userId);
        this.userSubscriptionRepository.save(userSubscription);
    }

    private User getUser(Long userId) {
        return this.userRepository.findById(userId).orElseThrow(
                () -> {
                    logger.error("User with id: {} not found.", userId);
                    return new UserNotFoundException("User with id " + userId + " not found.");
                }
        );
    }

    private Tariff validateAndGetTariff(Long tariffId) {
        Tariff tariff = this.tariffRepository.findById(tariffId).orElseThrow(
                () -> {
                    logger.error("Tariff with id: {} not found.", tariffId);
                    return new NotFoundException("Tariff with id " + tariffId + " not found.");
                }
        );

        if (!tariff.getActive()) {
            logger.warn("Tariff with id: {} is not active.", tariffId);
            throw new BusinessLogicException("Tariff with id: " + tariffId + " is not active.");
        }

        if (tariff.getType() != TariffType.SUBSCRIPTION) {
            logger.warn("Tariff with id: {} is not a subscription and cannot assigned.", tariffId);
            throw new BusinessLogicException("Tariff with id: " + tariffId + " is not a subscription.");
        }
        return tariff;
    }

    private UserSubscription createUserSubscription(Integer minutesUsageLimit, Tariff tariff, User user) {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(tariff.getSubDurationDays());
        int minuteUsed = 0;

        return new UserSubscription(
                null,
                user,
                tariff,
                minutesUsageLimit,
                minuteUsed,
                startDate,
                endDate
        );
    }
}
