package ru.noleg.scootrent.service.rental;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.repository.RentalRepository;
import ru.noleg.scootrent.service.RentalService;
import ru.noleg.scootrent.service.rental.pause.RentalPauser;
import ru.noleg.scootrent.service.rental.resume.RentalResumer;
import ru.noleg.scootrent.service.rental.start.RentalStarter;
import ru.noleg.scootrent.service.rental.stop.RentalStopper;

import java.util.List;

@Service
public class RentalServiceDefaultImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final RentalStarter rentalStarter;
    private final RentalPauser rentalPauser;
    private final RentalResumer rentalResumer;
    private final RentalStopper rentalStopper;


    public RentalServiceDefaultImpl(RentalRepository rentalRepository,
                                    RentalStarter rentalStarter,
                                    RentalPauser rentalPauser,
                                    RentalResumer rentalResumer,
                                    RentalStopper rentalStopper) {
        this.rentalRepository = rentalRepository;
        this.rentalStarter = rentalStarter;
        this.rentalPauser = rentalPauser;
        this.rentalResumer = rentalResumer;
        this.rentalStopper = rentalStopper;
    }


    @Override
    @Transactional
    public Long startRental(Long userId, Long scooterId, Long rentalPointId) {
        return this.rentalStarter.startRental(userId, scooterId, rentalPointId);
    }

    @Override
    @Transactional
    public void pauseRental(Long rentalId) {
        this.rentalPauser.pauseRental(rentalId);
    }

    @Override
    @Transactional
    public void resumeRental(Long rentalId) {
        this.rentalResumer.resumeRental(rentalId);
    }

    @Override
    @Transactional
    public void stopRental(Long rentalId, Long endPointId) {
        this.rentalStopper.stopRental(rentalId, endPointId);
    }

    @Override
    public List<Rental> getRentals() {
        return this.rentalRepository.findAllRentals();
    }

    @Override
    public List<Rental> getRentalHistoryForScooter(Long scooterId) {
        try {

            return this.rentalRepository.findRentalForScooter(scooterId);
        } catch (Exception e) {
            throw new ServiceException("Error on get rental history for scooter", e);
        }
    }

    @Override
    public List<Rental> getRentalHistoryForUser(Long userId) {
        try {
            return this.rentalRepository.findRentalsForUser(userId);
        } catch (Exception e) {
            throw new ServiceException("Error on get rental history for scooter", e);
        }
    }
}
