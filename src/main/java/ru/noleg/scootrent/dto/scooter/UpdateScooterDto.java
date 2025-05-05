package ru.noleg.scootrent.dto.scooter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.noleg.scootrent.entity.scooter.ScooterStatus;

@Schema(description = "Обновление самоката")
public record UpdateScooterDto(
        @Schema(description = "Номерной знак самоката", example = "T017PC") @NotBlank @Size(min = 4, max = 10)
        String numberPlate,

        @Schema(description = "Статус работы самоката", example = "TAKEN")
        ScooterStatus status,

        @Schema(description = "Id модели самоката", example = "1") @NotNull
        Long modelId,

        @Schema(description = "Id точки проката", example = "1") @NotNull
        Long rentalPointId
) {
}
