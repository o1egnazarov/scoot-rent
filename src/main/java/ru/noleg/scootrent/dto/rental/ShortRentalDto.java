package ru.noleg.scootrent.dto.rental;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.noleg.scootrent.dto.location.ShortLocationDto;
import ru.noleg.scootrent.dto.scooter.ShortScooterDtoWithModel;
import ru.noleg.scootrent.dto.tariff.ShortTariffDto;
import ru.noleg.scootrent.dto.user.ShortUserDto;
import ru.noleg.scootrent.entity.rental.RentalStatus;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Schema(description = "Информация об аренде")
public record ShortRentalDto(
        @Schema(description = "Id аренды", example = "1")
        Long id,

        @Schema(description = "Пользователь самоката")
        ShortUserDto user,

        @Schema(description = "Самокат")
        ShortScooterDtoWithModel scooter,

        @Schema(description = "Тариф по которому считается аренда")
        ShortTariffDto tariff,

        @Schema(description = "Точка старта аренды")
        ShortLocationDto startPoint,

        @Schema(description = "Точка окончания аренды")
        ShortLocationDto endPoint,

        @Schema(description = "Статус аренды")
        RentalStatus rentalStatus,

        @Schema(description = "Цена аренды", example = "250.25")
        BigDecimal cost,

        @Schema(description = "Продолжительность аренды", example = "PT1H")
        Duration durationOfRental,

        @Schema(description = "Начало аренды", example = "2025-05-01T16:51:45")
        LocalDateTime startTime,

        @Schema(description = "Конец аренды", example = "2025-05-01T17:51:45")
        LocalDateTime endTime,

        @Schema(description = "Продолжительность паузы", example = "PT30M")
        Duration durationInPause
) {
}
