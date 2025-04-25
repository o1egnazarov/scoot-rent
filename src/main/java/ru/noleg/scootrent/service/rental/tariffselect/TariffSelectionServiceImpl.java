package ru.noleg.scootrent.service.rental.tariffselect;

import org.springframework.stereotype.Service;
import ru.noleg.scootrent.entity.UserSubscription;
import ru.noleg.scootrent.entity.UserTariff;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.repository.TariffRepository;
import ru.noleg.scootrent.repository.UserSubscriptionRepository;
import ru.noleg.scootrent.repository.UserTariffRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TariffSelectionServiceImpl implements TariffSelectionService {

    private final TariffRepository tariffRepository;
    private final UserTariffRepository userTariffRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    public TariffSelectionServiceImpl(TariffRepository tariffRepository,
                                      UserTariffRepository userTariffRepository,
                                      UserSubscriptionRepository userSubscriptionRepository) {
        this.tariffRepository = tariffRepository;
        this.userTariffRepository = userTariffRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
    }

    @Override
    public SelectedTariff selectTariffForUser(Long userId) {
        try {

            Optional<UserSubscription> userSub =
                    this.userSubscriptionRepository.findActiveSubscriptionByUserAndTime(userId, LocalDateTime.now());

            if (userSub.isPresent()) {
                return new SelectedTariff(userSub.get().getTariff(), userSub.get());
            }

            Optional<UserTariff> userTariff =
                    this.userTariffRepository.findActiveTariffByUserAndTime(userId, LocalDateTime.now());

            if (userTariff.isPresent()) {
                return new SelectedTariff(userTariff.get().getTariff(), null);
            }

            // TODO добавить подгрузку default тарифа
            Tariff defaultTariff = this.tariffRepository.findById(1L).orElseThrow(
                    () -> new NotFoundException("Default tariff not found")
            );

            return new SelectedTariff(defaultTariff, null);
        } catch (NotFoundException e) {

            throw e;
        } catch (Exception e) {

            throw new ServiceException("Error on select tariff for user", e);
        }
    }

    public record SelectedTariff(Tariff tariff, UserSubscription subscription) {
    }
}
