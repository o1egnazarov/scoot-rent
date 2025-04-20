package ru.noleg.scootrent.service;

import ru.noleg.scootrent.entity.scooter.Scooter;

import java.util.List;

public interface ScooterService {
    Long add(Scooter scooter);

    void delete(Long id);

    Scooter getScooter(Long id);

    List<Scooter> getAllScooters();
}
