package ru.noleg.scootrent.service.rental.start;

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
import ru.noleg.scootrent.service.rental.tariffselect.TariffSelectionService;

import java.time.LocalDateTime;

@Service
public class RentalStarterImpl implements RentalStarter {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final ScooterRepository scooterRepository;
    private final RentalPointRepository rentalPointRepository;
    private final TariffSelectionService tariffSelectionService;

    public RentalStarterImpl(RentalRepository rentalRepository,
                             UserRepository userRepository,
                             ScooterRepository scooterRepository,
                             RentalPointRepository rentalPointRepository,
                             TariffSelectionService tariffSelectionService) {
        this.rentalRepository = rentalRepository;
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
        } catch (BusinessLogicException | NotFoundException e) {

            throw e;
        } catch (Exception e) {

            throw new ServiceException("Error on start rental", e);
        }
    }
}
