package ru.noleg.scootrent.dto.location;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.noleg.scootrent.entity.location.LocationType;

import java.math.BigDecimal;

@Schema(description = "Локация")
public record CreateLocationDto(
        @Schema(description = "Id локации", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Тип локации", example = "CITY") @NotNull
        LocationType locationType,

        @Schema(description = "Адрес локации", example = "Мира 55а") @NotBlank @Size(min = 5, max = 100)
        String address,

        @Schema(description = "Название локации", example = "Парковая зона") @NotBlank @Size(min = 5, max = 50)
        String title,

        @Schema(description = "Широта", example = "54.96305600")
        BigDecimal latitude,

        @Schema(description = "Долгота", example = "73.38416700")
        BigDecimal longitude,

        @Schema(description = "Id родительской локации", example = "1")
        Long parentId
) {
}
