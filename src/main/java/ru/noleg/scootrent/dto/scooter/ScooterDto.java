package ru.noleg.scootrent.dto.scooter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.noleg.scootrent.entity.scooter.ScooterStatus;

import java.time.Duration;

@Schema(description = "Самокат")
public record ScooterDto(
        @Schema(description = "Id самоката", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Номерной знак самоката", example = "T017PC") @NotBlank @Size(min = 4, max = 10)
        String numberPlate,

        @Schema(description = "Статус работы самоката", example = "TAKEN")
        ScooterStatus status,

        @Schema(description = "Время в работе", example = "PT25M00", accessMode = Schema.AccessMode.READ_ONLY)
        Duration durationInUsed,

        @Schema(description = "Id модели самоката", example = "1") @NotNull
        Long modelId,

        @Schema(description = "Id точки проката", example = "1") @NotNull
        Long rentalPointId
) {
}
