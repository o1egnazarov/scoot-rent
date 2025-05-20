package ru.noleg.scootrent.service.rental.pause;

public interface RentalPauser {
    void pauseRental(Long rentalId, Long userId);
}
