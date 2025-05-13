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

@Service
@Transactional
public class TariffAssignmentServiceDefaultImpl implements TariffAssignmentService {

    private static final Logger logger = LoggerFactory.getLogger(TariffAssignmentServiceDefaultImpl.class);

    private final UserRepository userRepository;
    private final TariffRepository tariffRepository;
    private final UserTariffRepository userTariffRepository;

    public TariffAssignmentServiceDefaultImpl(UserRepository userRepository,
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
}
