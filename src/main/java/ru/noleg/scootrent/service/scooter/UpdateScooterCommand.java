package ru.noleg.scootrent.service.scooter;

import ru.noleg.scootrent.entity.scooter.ScooterStatus;

import java.time.Duration;

public record UpdateScooterCommand(
    String numberPlate,
    ScooterStatus status,
    Duration durationInUsed,
    Long modelId,
    Long rentalPointId
) {}
