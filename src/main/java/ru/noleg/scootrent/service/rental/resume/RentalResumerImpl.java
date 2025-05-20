package ru.noleg.scootrent.service.rental.resume;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.rental.RentalStatus;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.RentalRepository;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Transactional
public class RentalResumerImpl implements RentalResumer {

    private static final Logger logger = LoggerFactory.getLogger(RentalResumerImpl.class);

    private final RentalRepository rentalRepository;

    public RentalResumerImpl(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Override
    public void resumeRental(Long rentalId, Long userId) {

        Rental rental = this.validateAndGetRental(rentalId, userId);

        this.validateRentalStatusForResume(rentalId, rental);

        this.updateRentalStatus(rental);
        logger.debug("Rental with id: {} successfully resume. Total pause time: {}",
                rentalId, rental.getDurationInPause());
    }

    private Rental validateAndGetRental(Long rentalId, Long userId) {
        Rental rental = this.rentalRepository.findById(rentalId).orElseThrow(
                () -> {
                    logger.error("Rental with id: {} not found.", rentalId);
                    return new NotFoundException("Rental with id: " + rentalId + " not found.");
                }
        );

        if (!this.rentalRepository.isRentalOwnedByUser(rentalId, userId)) {
            logger.warn("The user with id: {} does not have a rental with id: {}", userId, rentalId);
            throw new BusinessLogicException("The user with id: " + userId + "  does not have a rental with id: " + rentalId);
        }

        return rental;
    }

    private void validateRentalStatusForResume(Long rentalId, Rental rental) {
        if (rental.getRentalStatus() != RentalStatus.PAUSE) {
            logger.warn("Attempt resume rental with id: {}, in rental status: {}", rentalId, rental.getRentalStatus());
            throw new BusinessLogicException("Rental is already used.");
        }
    }

    private void updateRentalStatus(Rental rental) {
        rental.addPause(Duration.between(rental.getLastPauseTime(), LocalDateTime.now()));
        rental.setLastPauseTime(null);
        rental.setRentalStatus(RentalStatus.ACTIVE);

        this.rentalRepository.save(rental);
    }
}
