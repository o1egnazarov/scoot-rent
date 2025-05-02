package ru.noleg.scootrent.dto.scooter.scootermodel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Модель самоката")
public record ScooterModelDto(
        @Schema(description = "Id модели самоката", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Название модели самоката", example = "Xiaomi scoot pro1") @NotBlank @Size(min = 5, max = 50)
        String title,

        @Schema(description = "Максимальная скорость модели", example = "35") @NotNull @Min(5) @Max(40)
        Integer maxSpeed,

        @Schema(description = "Максимальное расстояние на одном аккумуляторе", example = "45") @NotNull @Min(5) @Max(100)
        Integer rangeKm
) {
}
