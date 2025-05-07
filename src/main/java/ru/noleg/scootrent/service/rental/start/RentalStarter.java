package ru.noleg.scootrent.service.rental.start;

public interface RentalStarter {
    Long startRental(Long userId, Long scooterId, Long startPointId);
}
