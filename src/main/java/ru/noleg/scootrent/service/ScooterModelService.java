package ru.noleg.scootrent.service;

import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.entity.scooter.ScooterModel;

import java.util.List;

public interface ScooterModelService {
    Long add(ScooterModel scooterModel);

    void delete(Long id);

    ScooterModel getScooterModel(Long id);

    List<ScooterModel> getAllScooterModels();
}
