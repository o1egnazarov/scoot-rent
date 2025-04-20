package ru.noleg.scootrent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.noleg.scootrent.entity.scooter.ScooterStatus;

public record ScooterDto(
        Long id,
        @NotBlank @Size(min = 4, max = 10) String numberPlate,
        ScooterStatus scooterStatus,
        @NotNull Long modelId) {
}
