package ru.noleg.scootrent.service;

import ru.noleg.scootrent.dto.ScooterRentalHistoryDto;
import ru.noleg.scootrent.dto.rental.ShortRentalDto;
import ru.noleg.scootrent.entity.rental.Rental;

import java.util.List;

public interface RentalService {
    Long startRental(Long userId, Long scooterId, Long rentalPointId);

    void pauseRental(Long rentalId);

    void resumeRental(Long rentalId);

    void stopRental(Long rentalId, Long rentalPointId);

    List<Rental> getRentals();

    List<Rental> getRentalHistoryForScooter(Long scooterId);

    List<Rental> getRentalHistoryForUser(Long userId);
}
