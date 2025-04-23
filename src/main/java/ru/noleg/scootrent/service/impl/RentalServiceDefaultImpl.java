package ru.noleg.scootrent.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.rental.RentalPoint;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.entity.scooter.ScooterStatus;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.exception.UserNotFoundException;
import ru.noleg.scootrent.repository.RentalPointRepository;
import ru.noleg.scootrent.repository.RentalRepository;
import ru.noleg.scootrent.repository.ScooterRepository;
import ru.noleg.scootrent.repository.UserRepository;
import ru.noleg.scootrent.service.BillingService;
import ru.noleg.scootrent.service.RentalService;
import ru.noleg.scootrent.service.TariffSelectionService;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentalServiceDefaultImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final BillingService billingService;
    private final UserRepository userRepository;
    private final ScooterRepository scooterRepository;
    private final RentalPointRepository rentalPointRepository;
    private final TariffSelectionService tariffSelectionService;

    public RentalServiceDefaultImpl(RentalRepository rentalRepository,
                                    BillingService billingService,
                                    UserRepository userRepository,
                                    ScooterRepository scooterRepository,
                                    RentalPointRepository rentalPointRepository,
                                    TariffSelectionService tariffSelectionService) {
        this.rentalRepository = rentalRepository;
        this.billingService = billingService;
        this.userRepository = userRepository;
        this.scooterRepository = scooterRepository;
        this.rentalPointRepository = rentalPointRepository;
        this.tariffSelectionService = tariffSelectionService;
    }


    @Override
    @Transactional
    public Long startRental(Long userId, Long scooterId, Long rentalPointId) {
        try {

            Scooter scooter = this.scooterRepository.findById(scooterId).orElseThrow(
                    () -> new NotFoundException("Scooter with id: " + scooterId + " not found.")
            );

            if (scooter.getStatus() != ScooterStatus.FREE) {
                throw new BusinessLogicException("Scooter with id: " + scooterId + " is not free.");
            }

            User user = this.userRepository.findById(userId).orElseThrow(
                    () -> new UserNotFoundException("User with id: " + userId + " not found.")
            );

            if (this.rentalRepository.isActiveRentalByUserId(userId)) {
                throw new BusinessLogicException("Rental for user with id: " + userId + " is already active.");
            }

            RentalPoint startPoint = this.rentalPointRepository.findById(rentalPointId).orElseThrow(
                    () -> new NotFoundException("Rental point with id: " + rentalPointId + " not found.")
            );

            var tariff = this.tariffSelectionService.selectTariffForUser(userId);

            // TODO додумать выбор tariff | sub
            Rental rental = new Rental(
                    null,
                    user,
                    scooter,
                    tariff.tariff(),
                    tariff.subscription(),
                    null,
                    LocalDateTime.now(),
                    null,
                    startPoint,
                    null
            );

            scooter.setStatus(ScooterStatus.TAKEN);
            this.scooterRepository.save(scooter);
            user.addRental(rental);
            this.userRepository.save(user);

            return this.rentalRepository.save(rental).getId();
        } catch (Exception e) {
            throw new ServiceException("Error on start rental", e);
        }
    }

    @Override
    @Transactional
    public void stopRental(Long rentalId, Long endPointId) {
        try {

            Rental rental = this.rentalRepository.findById(rentalId).orElseThrow(
                    () -> new NotFoundException("Rental with id: " + rentalId + " not found.")
            );

            RentalPoint endPoint = this.rentalPointRepository.findById(endPointId).orElseThrow(
                    () -> new NotFoundException("Rental point with id: " + endPointId + " not found.")
            );

            Duration rentalDuration = Duration.between(rental.getStartTime(), LocalDateTime.now());
            BigDecimal cost = this.billingService.calculateRentalCost(rental.getUser(), rentalDuration);
            rental.stopRental(endPoint, cost);

            rental.getScooter().setRentalPoint(endPoint);
            rental.getScooter().setStatus(ScooterStatus.FREE);
            this.rentalRepository.save(rental);
        } catch (Exception e) {
            throw new ServiceException("Error on stop rental", e);
        }
    }

    @Override
    public List<Rental> getRentals() {
        return this.rentalRepository.findAll();
    }
}
