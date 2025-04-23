package ru.noleg.scootrent.dto.scooter;

import ru.noleg.scootrent.entity.scooter.ScooterStatus;

public record ShortScooterDtoWithModel(Long id,
                                       String numberPlate,
                                       ScooterStatus status,
                                       String modelTitle) {
}
