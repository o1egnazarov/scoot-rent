package ru.noleg.scootrent.service.rental.resume;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.rental.RentalStatus;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.repository.RentalRepository;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class RentalResumerImpl implements RentalResumer {

    private final RentalRepository rentalRepository;

    public RentalResumerImpl(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Override
    @Transactional
    public void resumeRental(Long rentalId) {
        try {

            Rental rental = this.rentalRepository.findById(rentalId).orElseThrow(
                    () -> new NotFoundException("Rental with id: " + rentalId + " not found.")
            );

            if (rental.getRentalStatus() != RentalStatus.PAUSE) {
                throw new BusinessLogicException("Rental is already used.");
            }

            rental.addPause(Duration.between(rental.getLastPauseTime(), LocalDateTime.now()));
            rental.setLastPauseTime(null);
            rental.setRentalStatus(RentalStatus.ACTIVE);

            this.rentalRepository.save(rental);
        } catch (NotFoundException | BusinessLogicException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error on resume rental.", e);
        }
    }
}
