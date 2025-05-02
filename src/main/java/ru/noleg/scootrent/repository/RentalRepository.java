package ru.noleg.scootrent.repository;

import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.repository.util.BaseRepository;

import java.util.List;

public interface RentalRepository extends BaseRepository<Rental, Long> {
    boolean isActiveRentalByUserId(Long userId);

    List<Rental> findAllRentals();

    List<Rental> findRentalsForUser(Long userId);

    List<Rental> findRentalForScooter(Long scooterId);
}
