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
@Transactional
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
    public void stopRental(Long rentalId, Long endPointId, Long userId) {

        Rental rental = this.validateAndGetRental(rentalId, userId);
        LocationNode endPoint = this.validateAndGetLocation(endPointId, rental.getStartPoint());

        Duration rentalDuration = this.calculateRentalDuration(rental);

        BigDecimal cost = this.calculateCost(rentalId, rental.getUser(), rental.getTariff(), rentalDuration);

        this.completeRental(rental, endPoint, cost, rentalDuration);
        logger.debug("Rental with id: {} successfully stopped. Cost: {}, Duration: {}.", rentalId, cost, rentalDuration);
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
            throw new BusinessLogicException("The user with id: " + userId + " does not have a rental with id: " + rentalId);
        }

        if (rental.getRentalStatus() == RentalStatus.COMPLETED) {
            logger.warn("Rental with id: {} already completed.", rentalId);
            throw new BusinessLogicException("Rental is already completed.");
        }
        return rental;
    }

    private LocationNode validateAndGetLocation(Long endPointId, LocationNode startPoint) {
        LocationNode endPoint = this.locationRepository.findLocationById(endPointId).orElseThrow(
                () -> {
                    logger.error("Rental end point with id: {} not found.", endPointId);
                    return new NotFoundException("Rental point with id: " + endPointId + " not found.");
                }
        );

        LocationNode startCity = this.getCityAncestor(startPoint);
        LocationNode endCity = this.getCityAncestor(endPoint);

        if (startCity == null || endCity == null || !startCity.getId().equals(endCity.getId())) {
            logger.warn("Rental start and end points belong to different cities. Start: {}, End: {}", startCity, endCity);
            throw new BusinessLogicException("Start and end rental points must belong to the same city.");
        }

        if (endPoint.getLocationType() != LocationType.RENTAL_POINT) {
            logger.warn("Location with id: {} is not a rental point.", endPointId);
            throw new BusinessLogicException("Location is not a rental point.");
        }
        return endPoint;
    }

    private LocationNode getCityAncestor(LocationNode node) {
        LocationNode current = node;
        while (current != null && current.getLocationType() != LocationType.CITY) {
            current = current.getParent();
        }
        return current;
    }

    private Duration calculateRentalDuration(Rental rental) {
        Duration pause = rental.getDurationInPause();
        Duration totalDuration = Duration.between(rental.getStartTime(), LocalDateTime.now());
        return totalDuration.minus(pause);
    }

    private BigDecimal calculateCost(Long rentalId, User user, Tariff tariff, Duration rentalDuration) {
        try {
            return this.billingService.calculateRentalCost(user, tariff, rentalDuration);
        } catch (Exception e) {
            logger.error("Error in calculate cost for rental with id: {}", rentalId, e);
            throw new ServiceException("Error in BillingService by rental with id " + rentalId, e);
        }
    }

    private void completeRental(Rental rental, LocationNode endPoint, BigDecimal cost, Duration rentalDuration) {
        rental.stopRental(endPoint, cost, rentalDuration);

        Scooter scooter = rental.getScooter();
        scooter.addDurationInUsed(rentalDuration);
        scooter.setRentalPoint(endPoint);
        scooter.setStatus(ScooterStatus.FREE);

        this.rentalRepository.save(rental);
    }
}
