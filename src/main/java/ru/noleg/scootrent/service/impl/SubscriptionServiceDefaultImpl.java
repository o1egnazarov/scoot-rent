package ru.noleg.scootrent.service.impl;

import org.springframework.stereotype.Service;
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
import ru.noleg.scootrent.service.SubscriptionService;

import java.time.LocalDateTime;

@Service
public class SubscriptionServiceDefaultImpl implements SubscriptionService {

    private final UserRepository userRepository;
    private final TariffRepository tariffRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    public SubscriptionServiceDefaultImpl(UserRepository userRepository,
                                          TariffRepository tariffRepository,
                                          UserSubscriptionRepository userSubscriptionRepository) {
        this.userRepository = userRepository;
        this.tariffRepository = tariffRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    @Override
    public void subscribeUser(Long userId, Long tariffId) {
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User with id " + userId + " not found.")
        );

        Tariff tariff = this.tariffRepository.findById(tariffId).orElseThrow(
                () -> new NotFoundException("Tariff with id " + tariffId + " not found.")
        );

        if (!tariff.getActive()) {
            throw new BusinessLogicException("Tariff with id: " + tariffId + " is not active.");
        }

        if (tariff.getType() != TariffType.SUBSCRIPTION) {
            throw new BusinessLogicException("Tariff with id: " + tariffId + " is not a subscription.");
        }

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(tariff.getSubDurationDays());
        int minuteUsed = 0;

        UserSubscription userSubscription = new UserSubscription(
                null,
                user,
                tariff,
                tariff.getDurationValue(),
                minuteUsed,
                startDate,
                endDate
        );

        this.userSubscriptionRepository.save(userSubscription);
    }
}
