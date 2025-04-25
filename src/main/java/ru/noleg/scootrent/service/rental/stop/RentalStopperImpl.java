package ru.noleg.scootrent.service.rental.stop;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.rental.RentalPoint;
import ru.noleg.scootrent.entity.scooter.ScooterStatus;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.repository.RentalPointRepository;
import ru.noleg.scootrent.repository.RentalRepository;
import ru.noleg.scootrent.service.rental.billing.BillingService;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class RentalStopperImpl implements RentalStopper {

    private final RentalRepository rentalRepository;
    private final BillingService billingService;
    private final RentalPointRepository rentalPointRepository;

    public RentalStopperImpl(RentalRepository rentalRepository,
                             BillingService billingService,
                             RentalPointRepository rentalPointRepository) {
        this.rentalRepository = rentalRepository;
        this.billingService = billingService;
        this.rentalPointRepository = rentalPointRepository;
    }

    @Override
    @Transactional
    public void stopRental(Long rentalId, Long endPointId) {
        try {

            Rental rental = this.rentalRepository.findById(rentalId).orElseThrow(
                    () -> new NotFoundException("Rental with id: " + rentalId + " not found.")
            );

            RentalPoint endPoint = this.rentalPointRepository.findById(endPointId).orElseThrow(
                    () -> new NotFoundException("Rental point with id: " + endPointId + " not found.")
            );

            Duration pause = rental.getDurationInPause();
            Duration totalDuration = Duration.between(rental.getStartTime(), LocalDateTime.now());
            Duration rentalDuration = totalDuration.minus(pause);

            BigDecimal cost = this.billingService.calculateRentalCost(rental.getUser(), rentalDuration);
            rental.stopRental(endPoint, cost);

            rental.getScooter().addDurationInUsed(rentalDuration);
            rental.getScooter().setRentalPoint(endPoint);
            rental.getScooter().setStatus(ScooterStatus.FREE);

            this.rentalRepository.save(rental);
        } catch (NotFoundException e) {

            throw e;
        } catch (Exception e) {

            throw new ServiceException("Error on stop rental", e);
        }
    }
}
