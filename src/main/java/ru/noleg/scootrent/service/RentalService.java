package ru.noleg.scootrent.service;

import ru.noleg.scootrent.entity.rental.Rental;

import java.util.List;

public interface RentalService {
    Long startRental(Long userId, Long scooterId, Long rentalPointId);

    void stopRental(Long rentalId, Long rentalPointId);

    List<Rental> getRentals();
}
