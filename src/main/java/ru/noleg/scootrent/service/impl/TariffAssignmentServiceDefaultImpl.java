package ru.noleg.scootrent.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.UserTariff;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.exception.UserNotFoundException;
import ru.noleg.scootrent.repository.TariffRepository;
import ru.noleg.scootrent.repository.UserRepository;
import ru.noleg.scootrent.repository.UserTariffRepository;
import ru.noleg.scootrent.service.TariffAssignmentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TariffAssignmentServiceDefaultImpl implements TariffAssignmentService {

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
    @Transactional
    public void assignTariffToUser(Long userId, Long tariffId, BigDecimal customPrice, Integer discountPct) {
        try {

            User user = this.userRepository.findById(userId).orElseThrow(
                    () -> new UserNotFoundException("User with id " + userId + " not found.")
            );

            Tariff tariff = this.tariffRepository.findById(tariffId).orElseThrow(
                    () -> new NotFoundException("Tariff with id " + tariffId + " not found.")
            );

            if (!tariff.getActive()) {
                throw new BusinessLogicException("Tariff with id: " + tariffId + " is not active.");
            }

            if (tariff.getType() == TariffType.SUBSCRIPTION) {
                throw new BusinessLogicException("Tariff with id: " + tariffId + " is a subscription.");
            }

            UserTariff userTariff = new UserTariff(
                    null,
                    user,
                    tariff,
                    discountPct,
                    customPrice,
                    LocalDateTime.now(),
                    tariff.getValidUntil()
            );

            this.userTariffRepository.save(userTariff);
        } catch (UserNotFoundException | NotFoundException | BusinessLogicException e) {

            throw e;
        } catch (Exception e) {

            throw new ServiceException("Error in assign tariff to user.");
        }
    }
}
