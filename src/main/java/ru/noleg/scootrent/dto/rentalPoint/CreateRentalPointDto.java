package ru.noleg.scootrent.dto.rentalPoint;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Точка проката")
public record CreateRentalPointDto(
        @Schema(description = "Id точки проката", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Адрес точки проката", example = "Мира 55а") @NotBlank @Size(min = 5, max = 100)
        String address,

        @Schema(description = "Название точки проката", example = "Парковая зона") @NotBlank @Size(min = 5, max = 50)
        String title,

        @Schema(description = "Широта", example = "54.96305600")
        BigDecimal latitude,

        @Schema(description = "Долгота", example = "73.38416700")
        BigDecimal longitude,

        @Schema(description = "Id родительской точки проката", example = "1")
        Long parentId
) {
}
