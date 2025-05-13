package ru.noleg.scootrent.service.rental.pause;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.rental.RentalStatus;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.RentalRepository;

import java.time.LocalDateTime;

@Service
public class RentalPauserImpl implements RentalPauser {

    private static final Logger logger = LoggerFactory.getLogger(RentalPauserImpl.class);

    private final RentalRepository rentalRepository;

    public RentalPauserImpl(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Override
    @Transactional
    public void pauseRental(Long rentalId) {

        Rental rental = this.rentalRepository.findById(rentalId).orElseThrow(
                () -> {
                    logger.error("Rental with id: {} not found.", rentalId);
                    return new NotFoundException("Rental with id: " + rentalId + " not found.");
                }
        );

        this.validateRentalStatusForPause(rentalId, rental);
        logger.debug("Rental status: {}.", rental.getRentalStatus());

        this.updateRentalStatus(rental);
        logger.debug("Rental with id: {} successfully pause. Pause time: {}.", rentalId, rental.getLastPauseTime());
    }

    private void validateRentalStatusForPause(Long rentalId, Rental rental) {
        if (rental.getRentalStatus() == RentalStatus.COMPLETED) {
            logger.warn("Attempt pause already completed rental with id: {}.", rentalId);
            throw new BusinessLogicException("Rental is already completed.");
        }

        if (rental.getRentalStatus() == RentalStatus.PAUSE) {
            logger.warn("Attempt pause already pause rental with id: {}.", rentalId);
            throw new BusinessLogicException("Rental is already paused.");
        }
    }

    private void updateRentalStatus(Rental rental) {
        rental.setRentalStatus(RentalStatus.PAUSE);
        rental.setLastPauseTime(LocalDateTime.now());
        this.rentalRepository.save(rental);
    }
}
