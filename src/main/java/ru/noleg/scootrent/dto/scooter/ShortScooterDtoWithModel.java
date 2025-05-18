package ru.noleg.scootrent.dto.scooter;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.noleg.scootrent.entity.scooter.ScooterStatus;

@Schema(description = "Краткая информация о самокате")
public record ShortScooterDtoWithModel(
        @Schema(description = "Id самоката", example = "1")
        Long id,

        @Schema(description = "Номерной знак самоката", example = "T017PC")
        String numberPlate,

        @Schema(description = "Статус работы самоката", example = "TAKEN")
        ScooterStatus status,

        @Schema(description = "Название модели", example = "Xiaomi scoot m1")
        String modelTitle
) {
}
