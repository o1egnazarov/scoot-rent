package ru.noleg.scootrent.repository;

import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.repository.util.BaseRepository;

public interface RentalRepository extends BaseRepository<Rental, Long> {
    boolean isActiveRentalByUserId(Long userId);
}
