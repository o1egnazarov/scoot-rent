package ru.noleg.scootrent.service.rental.pause;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.rental.RentalStatus;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.repository.RentalRepository;

import java.time.LocalDateTime;

@Service
public class RentalPauserImpl implements RentalPauser {

    private final RentalRepository rentalRepository;

    public RentalPauserImpl(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Override
    @Transactional
    public void pauseRental(Long rentalId) {
        try {

            Rental rental = this.rentalRepository.findById(rentalId).orElseThrow(
                    () -> new NotFoundException("Rental with id: " + rentalId + " not found.")
            );

            if (rental.getRentalStatus() == RentalStatus.COMPLETED) {
                throw new BusinessLogicException("Rental is already completed.");
            }

            if (rental.getRentalStatus() == RentalStatus.PAUSE) {
                throw new BusinessLogicException("Rental is already paused.");
            }

            rental.setRentalStatus(RentalStatus.PAUSE);
            rental.setLastPauseTime(LocalDateTime.now());
            this.rentalRepository.save(rental);
        } catch (NotFoundException | BusinessLogicException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error on pause rental.", e);
        }
    }
}
