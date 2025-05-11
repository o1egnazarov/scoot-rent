package ru.noleg.scootrent.service.rental;

import ru.noleg.scootrent.entity.rental.Rental;

import java.util.List;

public interface RentalService {
    Long startRental(Long userId, Long scooterId, Long startPointId);

    void pauseRental(Long rentalId);

    void resumeRental(Long rentalId);

    void stopRental(Long rentalId, Long endPointId);

    List<Rental> getRentals();

    List<Rental> getRentalHistoryForScooter(Long scooterId);

    List<Rental> getRentalHistoryForUser(Long userId);
}
