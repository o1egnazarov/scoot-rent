package ru.noleg.scootrent.service.rental.stop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.location.LocationType;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.rental.RentalStatus;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.entity.scooter.ScooterStatus;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.repository.LocationRepository;
import ru.noleg.scootrent.repository.RentalRepository;
import ru.noleg.scootrent.service.rental.billing.BillingService;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class RentalStopperImpl implements RentalStopper {

    private static final Logger logger = LoggerFactory.getLogger(RentalStopperImpl.class);

    private final RentalRepository rentalRepository;
    private final BillingService billingService;
    private final LocationRepository locationRepository;

    public RentalStopperImpl(RentalRepository rentalRepository,
                             BillingService billingService,
                             LocationRepository locationRepository) {
        this.rentalRepository = rentalRepository;
        this.billingService = billingService;
        this.locationRepository = locationRepository;
    }

    @Override
    @Transactional
    public void stopRental(Long rentalId, Long endPointId) {

        Rental rental = this.validateAndGetRental(rentalId);
        LocationNode endPoint = this.validateAndGetLocation(endPointId);

        Duration rentalDuration = this.calculateRentalDuration(rental);

        BigDecimal cost = this.calculateCost(rentalId, rental.getUser(), rental.getTariff(), rentalDuration);

        this.completeRental(rental, endPoint, cost, rentalDuration);
        logger.debug("Аренда id: {} успешно завершена. Стоимость: {}, Длительность: {}.", rentalId, cost, rentalDuration);
    }

    private Rental validateAndGetRental(Long rentalId) {
        Rental rental = this.rentalRepository.findById(rentalId).orElseThrow(
                () -> {
                    logger.error("Аренда с id: {} не найдена.", rentalId);
                    return new NotFoundException("Rental with id: " + rentalId + " not found.");
                }
        );

        if (rental.getRentalStatus() == RentalStatus.COMPLETED) {
            logger.warn("Аренда с id: {} уже завершена.", rentalId);
            throw new BusinessLogicException("Rental is already completed.");
        }
        return rental;
    }

    private LocationNode validateAndGetLocation(Long endPointId) {
        LocationNode endPoint = this.locationRepository.findById(endPointId).orElseThrow(
                () -> {
                    logger.error("Точка завершения аренды с id: {} не найдена", endPointId);
                    return new NotFoundException("Rental point with id: " + endPointId + " not found.");
                }
        );

        if (endPoint.getLocationType() != LocationType.RENTAL_POINT) {
            logger.warn("Локация с id: {} не является точкой аренды.", endPointId);
            throw new BusinessLogicException("Location is not a rental point.");
        }
        return endPoint;
    }

    private Duration calculateRentalDuration(Rental rental) {
        Duration pause = rental.getDurationInPause();
        Duration totalDuration = Duration.between(rental.getStartTime(), LocalDateTime.now());
        return totalDuration.minus(pause);
    }

    // TODO доработать
    private BigDecimal calculateCost(Long rentalId, User user, Tariff tariff, Duration rentalDuration) {
        try {
            return this.billingService.calculateRentalCost(user, tariff, rentalDuration);
        } catch (Exception e) {
            logger.error("Ошибка в расчете стоимости для аренды с id: {}", rentalId, e);
            throw new ServiceException("Ошибка в сервисе расчета стоимости за аренду с id " + rentalId, e);
        }
    }

    private void completeRental(Rental rental, LocationNode endPoint, BigDecimal cost, Duration rentalDuration) {
        rental.stopRental(endPoint, cost, rentalDuration);

        // TODO проследить за самокатом (за его сохранением)
        Scooter scooter = rental.getScooter();
        scooter.addDurationInUsed(rentalDuration);
        scooter.setRentalPoint(endPoint);
        scooter.setStatus(ScooterStatus.FREE);

        this.rentalRepository.save(rental);
    }
}
