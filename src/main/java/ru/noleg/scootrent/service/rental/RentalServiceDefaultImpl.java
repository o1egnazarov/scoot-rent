package ru.noleg.scootrent.service.rental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.repository.RentalRepository;
import ru.noleg.scootrent.service.rental.pause.RentalPauser;
import ru.noleg.scootrent.service.rental.resume.RentalResumer;
import ru.noleg.scootrent.service.rental.start.RentalStarter;
import ru.noleg.scootrent.service.rental.stop.RentalStopper;

import java.util.List;

@Service
@Transactional
public class RentalServiceDefaultImpl implements RentalService {

    private static final Logger logger = LoggerFactory.getLogger(RentalServiceDefaultImpl.class);

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
    public Long startRental(Long userId, Long scooterId, Long rentalPointId, BillingMode billingMode) {
        logger.debug("Start rental. User: {}, Scooter: {}, RentalPoint: {}.", userId, scooterId, rentalPointId);
        return this.rentalStarter.startRental(userId, scooterId, rentalPointId, billingMode);
    }

    @Override
    public void pauseRental(Long rentalId) {
        logger.debug("Pauser rental with id: {}.", rentalId);
        this.rentalPauser.pauseRental(rentalId);
    }

    @Override
    public void resumeRental(Long rentalId) {
        logger.debug("Resume rental with id: {}.", rentalId);
        this.rentalResumer.resumeRental(rentalId);
    }

    @Override
    public void stopRental(Long rentalId, Long endPointId) {
        logger.debug("Stop rental with id: {}, rental point: {}.", rentalId, endPointId);
        this.rentalStopper.stopRental(rentalId, endPointId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rental> getRentals() {
        logger.debug("Get all rentals.");
        return this.rentalRepository.findAllRentals();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rental> getRentalHistoryForScooter(Long scooterId) {
        logger.info("Get rental history for scooter with id: {}.", scooterId);
        return this.rentalRepository.findRentalForScooter(scooterId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rental> getRentalHistoryForUser(Long userId) {
        logger.info("Get rental history for user with id: {}.", userId);
        return this.rentalRepository.findRentalsForUser(userId);
    }
}

