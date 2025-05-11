package ru.noleg.scootrent.service.rental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.repository.RentalRepository;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.service.rental.pause.RentalPauser;
import ru.noleg.scootrent.service.rental.resume.RentalResumer;
import ru.noleg.scootrent.service.rental.start.RentalStarter;
import ru.noleg.scootrent.service.rental.stop.RentalStopper;

import java.util.List;

@Service
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
    @Transactional
    public Long startRental(Long userId, Long scooterId, Long rentalPointId, BillingMode billingMode) {
        logger.debug("Запрос на начало аренды. Пользователь: {}, Самокат: {}, Точка: {}.", userId, scooterId, rentalPointId);
        return this.rentalStarter.startRental(userId, scooterId, rentalPointId, billingMode);
    }

    @Override
    @Transactional
    public void pauseRental(Long rentalId) {
        logger.debug("Запрос на приостановку аренды id: {}.", rentalId);
        this.rentalPauser.pauseRental(rentalId);
    }

    @Override
    @Transactional
    public void resumeRental(Long rentalId) {
        logger.debug("Запрос на возобновление аренды id: {}.", rentalId);
        this.rentalResumer.resumeRental(rentalId);
    }

    @Override
    @Transactional
    public void stopRental(Long rentalId, Long endPointId) {
        logger.debug("Запрос на завершение аренды id: {}, точка: {}.", rentalId, endPointId);
        this.rentalStopper.stopRental(rentalId, endPointId);
    }

    @Override
    public List<Rental> getRentals() {
        logger.debug("Запрос всех активных аренд.");
        return this.rentalRepository.findAllRentals();
    }

    @Override
    public List<Rental> getRentalHistoryForScooter(Long scooterId) {
        logger.info("Запрос истории аренд для самоката id: {}.", scooterId);
        return this.rentalRepository.findRentalForScooter(scooterId);
    }

    @Override
    public List<Rental> getRentalHistoryForUser(Long userId) {
        logger.info("Запрос истории аренд для пользователя id: {}.", userId);
        return this.rentalRepository.findRentalsForUser(userId);
    }
}
