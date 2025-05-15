package ru.noleg.scootrent.service.tariff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.UserTariff;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.UserNotFoundException;
import ru.noleg.scootrent.repository.TariffRepository;
import ru.noleg.scootrent.repository.UserRepository;
import ru.noleg.scootrent.repository.UserTariffRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class TariffAssignmentServiceImpl implements TariffAssignmentService {

    private static final Logger logger = LoggerFactory.getLogger(TariffAssignmentServiceImpl.class);

    private final UserRepository userRepository;
    private final TariffRepository tariffRepository;
    private final UserTariffRepository userTariffRepository;

    public TariffAssignmentServiceImpl(UserRepository userRepository,
                                       TariffRepository tariffRepository,
                                       UserTariffRepository userTariffRepository) {
        this.userRepository = userRepository;
        this.tariffRepository = tariffRepository;
        this.userTariffRepository = userTariffRepository;
    }

    @Override
    public void assignTariffToUser(Long userId, Long tariffId, BigDecimal customPrice, Integer discountPct) {
        logger.debug("Assign tariff with new price = {}, discount = {}.", customPrice, discountPct);

        User user = this.getUser(userId);
        this.validateCurrentTariffs(userId);
        Tariff tariff = this.validateAndGetTariff(tariffId);

        UserTariff userTariff = this.createUserTariff(customPrice, discountPct, user, tariff);
        this.userTariffRepository.save(userTariff);
        logger.debug("Tariff {} successfully assigned to user {}.", tariffId, userId);
    }

    private User getUser(Long userId) {
        return this.userRepository.findById(userId).orElseThrow(
                () -> {
                    logger.error("User with id: {} not found.", userId);
                    return new UserNotFoundException("User with id " + userId + " not found.");
                }
        );
    }

    // TODO додумать
    private void validateCurrentTariffs(Long userId) {
        Optional<UserTariff> existing = userTariffRepository.findActiveTariffByUserAndTime(userId, LocalDateTime.now());
        existing.ifPresent(userTariff -> {
            logger.warn("User already been assigned a special tariff with id: {}.", userId);
            throw new BusinessLogicException("User already been assigned a special tariff with id: " + userTariff.getId());
        });
    }

    private Tariff validateAndGetTariff(Long tariffId) {
        Tariff tariff = this.tariffRepository.findById(tariffId).orElseThrow(
                () -> {
                    logger.error("Tariff with id: {} not found.", tariffId);
                    return new NotFoundException("Tariff with id " + tariffId + " not found.");
                }
        );

        if (!tariff.getIsActive()) {
            logger.warn("Tariff with id: {} is not active.", tariffId);
            throw new BusinessLogicException("Tariff with id: " + tariffId + " is not active.");
        }

        if (tariff.getType() == TariffType.SUBSCRIPTION) {
            logger.warn("Tariff with id: {} is a subscription and cannot assigned.", tariffId);
            throw new BusinessLogicException("Tariff with id: " + tariffId + " is a subscription.");
        }
        return tariff;
    }

    private UserTariff createUserTariff(BigDecimal customPrice, Integer discountPct, User user, Tariff tariff) {
        UserTariff userTariff = new UserTariff();
        userTariff.setUser(user);
        userTariff.setTariff(tariff);
        userTariff.setDiscountPct(discountPct);
        userTariff.setCustomPricePerMinute(customPrice);
        userTariff.setStartDate(LocalDateTime.now());
        userTariff.setEndDate(userTariff.getCalculatedEndDate());
        return userTariff;
    }

    @Override
    @Transactional(readOnly = true)
    public UserTariff getActiveUserTariff(Long userId) {
        return this.userTariffRepository.findActiveTariffByUserAndTime(userId, LocalDateTime.now()).orElseThrow(
                () -> {
                    logger.error("Special tariff not found for user with id: {}.", userId);
                    return new NotFoundException("Special tariff not found for user with id: " + userId);
                }
        );
    }

    @Override
    public void canselTariffFromUser(Long userId) {
        logger.debug("Cansel tariff from user {}.", userId);

        UserTariff userTariff =
                this.userTariffRepository.findActiveTariffByUserAndTime(userId, LocalDateTime.now()).orElseThrow(
                        () -> {
                            logger.error("Special tariff not found for user with id: {}.", userId);
                            return new NotFoundException("Special tariff not found for user with id: " + userId);
                        }
                );

        this.userTariffRepository.delete(userTariff.getId());
        logger.debug("Tariff {} successfully canselt.", userTariff.getId());
    }
}
