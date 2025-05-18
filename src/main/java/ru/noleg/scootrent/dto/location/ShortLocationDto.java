package ru.noleg.scootrent.dto.location;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.noleg.scootrent.entity.location.LocationType;

@Schema(description = "Краткая информация о локации")
public record ShortLocationDto(
        @Schema(description = "Id локации", example = "1")
        Long id,

        @Schema(description = "Название локации", example = "Парковая зона")
        String title,

        @Schema(description = "Тип локации", example = "CITY")
        LocationType locationType,

        @Schema(description = "Адрес локации", example = "Мира 55а")
        String address
) {
}
