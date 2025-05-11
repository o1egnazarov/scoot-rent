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
public class RentalResumerImpl implements RentalResumer {

    private static final Logger logger = LoggerFactory.getLogger(RentalResumerImpl.class);

    private final RentalRepository rentalRepository;

    public RentalResumerImpl(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Override
    @Transactional
    public void resumeRental(Long rentalId) {

        Rental rental = this.rentalRepository.findById(rentalId).orElseThrow(
                () -> {
                    logger.error("Аренда с id: {} не найдена.", rentalId);
                    return new NotFoundException("Rental with id: " + rentalId + " not found.");
                }
        );

        this.validateRentalStatusForResume(rentalId, rental);

        this.updateRentalStatus(rental);
        logger.debug("Аренда с ID: {} успешно возобновлена. Общее время приостановки: {}",
                rentalId, Duration.between(rental.getLastPauseTime(), LocalDateTime.now()));
    }

    private void validateRentalStatusForResume(Long rentalId, Rental rental) {
        if (rental.getRentalStatus() != RentalStatus.PAUSE) {
            logger.warn("Попытка возобновить аренду с id: {}, в статусе: {}", rentalId, rental.getRentalStatus());
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
