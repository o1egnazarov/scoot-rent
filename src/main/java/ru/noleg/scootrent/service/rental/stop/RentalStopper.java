package ru.noleg.scootrent.service.rental.stop;

public interface RentalStopper {
    void stopRental(Long rentalId, Long endPointId, Long userId);
}
