package ru.noleg.scootrent.dto.scooter;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.noleg.scootrent.entity.scooter.ScooterModel;
import ru.noleg.scootrent.entity.scooter.ScooterStatus;

import java.time.Duration;

@Schema(description = "Самокат с моделью")
public record ScooterDtoWithModel(
        @Schema(description = "Id самоката", example = "1")
        Long id,

        @Schema(description = "Номерной знак самоката", example = "T017PC")
        String numberPlate,

        @Schema(description = "Статус работы самоката", example = "TAKEN")
        ScooterStatus status,

        @Schema(description = "Время в работе", example = "PT45M")
        Duration durationInUsed,

        @Schema(description = "Модель самоката", example = "1")
        ScooterModel model
) {
}
