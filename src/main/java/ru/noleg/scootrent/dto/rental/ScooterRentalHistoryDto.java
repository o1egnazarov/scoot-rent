package ru.noleg.scootrent.dto.rental;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.noleg.scootrent.entity.rental.RentalStatus;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Schema(description = "История аренды самоката")
public record ScooterRentalHistoryDto(
        @Schema(description = "Id аренды", example = "1")
        Long rentalId,

        @Schema(description = "Имя арендатора", example = "username123")
        String username,

        @Schema(description = "Номер телефона пользователя", example = "+79541180360")
        String phone,

        @Schema(description = "Общее время использования самоката", example = "PT1H")
        Duration durationOfUsedScooter,

        @Schema(description = "Начало аренды", example = "2025-05-01T16:51:45")
        LocalDateTime startTime,

        @Schema(description = "Конец аренды", example = "2025-05-01T17:51:45")
        LocalDateTime endTime,

        @Schema(description = "Продолжительность аренды", example = "PT1H")
        Duration duration,

        @Schema(description = "Цена аренды", example = "250.25")
        BigDecimal cost,

        @Schema(description = "Название стартовой точки", example = "ул. Ленина, 21")
        String startPointTitle,

        @Schema(description = "Название конечной точки", example = "ул. Красный путь, 10")
        String endPointTitle,

        @Schema(description = "Название тарифа", example = "Бесплатные выходные")
        String tariffTitle,

        @Schema(description = "Статус аренды", example = "COMPLETED")
        RentalStatus rentalStatus
) {
}