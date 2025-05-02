package ru.noleg.scootrent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.rental.ShortRentalDto;
import ru.noleg.scootrent.mapper.RentalMapper;
import ru.noleg.scootrent.service.RentalService;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@Validated
@Tag(
        name = "Контроллер для аренды самокатов.",
        description = "Позволяет начать/приостановить/закончить аренду."
)
public class RentalController {

    private final RentalService rentalService;
    private final RentalMapper rentalMapper;

    public RentalController(RentalService rentalService, RentalMapper rentalMapper) {
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
    }

    @PostMapping("/start")
    @Operation(
            summary = "Начало аренды.",
            description = "Позволяет начать аренду для конкретного пользователя, самоката и точки проката."
    )
    public ResponseEntity<Long> startRental(
            @Parameter(description = "Идентификатор аренды", required = true) @Min(1) @RequestParam("userId") Long userId,
            @Parameter(description = "Идентификатор самоката", required = true) @Min(1) @RequestParam("scooterId") Long scooterId,
            @Parameter(description = "Идентификатор точки доступа", required = true) @Min(1) @RequestParam("rentalPointId") Long rentalPointId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.rentalService.startRental(userId, scooterId, rentalPointId));
    }

    @PostMapping("/pause")
    @Operation(
            summary = "Приостановление аренды.",
            description = "Позволяет поставить конкретную аренду на паузу."
    )
    public ResponseEntity<Void> pauseRental(
            @Parameter(description = "Идентификатор аренды", required = true) @Min(1) @RequestParam("rentalId") Long rentalId
    ) {
        this.rentalService.pauseRental(rentalId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/resume")
    @Operation(
            summary = "Возобновление аренды.",
            description = "Позволяет возобновить конкретную аренду."
    )
    public ResponseEntity<Void> resumeRental(
            @Parameter(description = "Идентификатор аренды", required = true) @Min(1) @RequestParam("rentalId") Long rentalId
    ) {
        this.rentalService.resumeRental(rentalId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/end")
    @Operation(
            summary = "Окончание аренды.",
            description = "Позволяет закончить конкретную аренду на конкретной точке проката."
    )
    public ResponseEntity<Void> endRental(
            @Parameter(description = "Идентификатор аренды", required = true) @Min(1) @RequestParam("rentalId") Long rentalId,
            @Parameter(description = "Идентификатор точки проката", required = true) @Min(1) @RequestParam("rentalPointId") Long rentalPointId
    ) {

        this.rentalService.stopRental(rentalId, rentalPointId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    @Operation(
            summary = "Получение всех аренд.",
            description = "Скорее всего будет убрано в админ-панель."
    )
    public List<ShortRentalDto> getRentals() {
        return this.rentalMapper.mapToShortDtos(this.rentalService.getRentals());
    }
}
