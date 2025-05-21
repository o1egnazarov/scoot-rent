package ru.noleg.scootrent.service.scooter;

import ru.noleg.scootrent.entity.scooter.Scooter;

import java.util.List;

public interface ScooterService {
    Long add(Scooter scooter);

    Scooter update(Long id, UpdateScooterCommand updateScooterCommand);

    void delete(Long id);

    Scooter getScooter(Long id);

    List<Scooter> getAllScooters();
}
