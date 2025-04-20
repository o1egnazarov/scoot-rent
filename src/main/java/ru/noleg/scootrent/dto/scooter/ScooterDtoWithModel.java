package ru.noleg.scootrent.dto.scooter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.noleg.scootrent.entity.scooter.ScooterModel;
import ru.noleg.scootrent.entity.scooter.ScooterStatus;

public record ScooterDtoWithModel(Long id,
                                  @NotBlank @Size(min = 4, max = 10) String numberPlate,
                                  ScooterStatus status,
                                  ScooterModel model) {
}
