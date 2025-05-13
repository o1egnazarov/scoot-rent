package ru.noleg.scootrent.dto.location;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.noleg.scootrent.dto.scooter.ScooterDtoWithModel;
import ru.noleg.scootrent.entity.location.LocationType;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Детальная информация о локации")
public record DetailLocationDto(
        @Schema(description = "Id локации", example = "1")
        Long id,

        @Schema(description = "Название локации", example = "Парковая зона")
        String title,

        @Schema(description = "Тип локации", example = "CITY")
        LocationType locationType,

        @Schema(description = "Широта", example = "54.96305600")
        BigDecimal latitude,

        @Schema(description = "Долгота", example = "73.38416700")
        BigDecimal longitude,

        @Schema(description = "Адрес локации", example = "Мира 55а")
        String address,

        @Schema(description = "Самокаты на точке проката")
        List<ScooterDtoWithModel> scooters,

        @Schema(description = "Количество самокатов в точке проката", example = "1")
        int totalCount
) {
}
