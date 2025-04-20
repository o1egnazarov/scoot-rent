package ru.noleg.scootrent.repository;

import ru.noleg.scootrent.entity.rental.Rental;

import java.util.List;
import java.util.Optional;

public interface RentalRepository {
    Long save(Rental rental);

    void removeById(Long id);

    List<Rental> getAll();

    Optional<Rental> findById(Long id);
}
