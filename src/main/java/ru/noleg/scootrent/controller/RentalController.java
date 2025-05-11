package ru.noleg.scootrent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.rental.ShortRentalDto;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.mapper.RentalMapper;
import ru.noleg.scootrent.service.rental.RentalService;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@Validated
@Tag(
        name = "Контроллер для аренды самокатов.",
        description = "Позволяет начать/приостановить/закончить аренду."
)
public class RentalController {

    private static final Logger logger = LoggerFactory.getLogger(RentalController.class);

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
            @Parameter(description = "Идентификатор пользователя", required = true) @Min(1) @RequestParam("userId") Long userId,
            @Parameter(description = "Идентификатор самоката", required = true) @Min(1) @RequestParam("scooterId") Long scooterId,
            @Parameter(description = "Идентификатор стартовой точки аренды", required = true) @Min(1) @RequestParam("startPointId") Long startPointId
    ) {
        logger.info("Полученный запрос: POST /start с параметрами: userId={}, scooterId={}, rentalPointId={}.", userId, scooterId, startPointId);
        Long rentalId = this.rentalService.startRental(userId, scooterId, startPointId);

        logger.debug("Аренда начата успешно. rentalId={}.", rentalId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rentalId);
    }

    @PostMapping("/pause")
    @Operation(
            summary = "Приостановление аренды.",
            description = "Позволяет поставить конкретную аренду на паузу."
    )
    public ResponseEntity<Void> pauseRental(
            @Parameter(description = "Идентификатор аренды", required = true) @Min(1) @RequestParam("rentalId") Long rentalId
    ) {
        logger.info("Полученный запрос: POST /pause с параметрами: rentalId={}.", rentalId);
        this.rentalService.pauseRental(rentalId);

        logger.debug("Аренда приостановлена: rentalId={}.", rentalId);
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
        logger.info("Полученный запрос: POST /resume с параметрами: rentalId={}.", rentalId);
        this.rentalService.resumeRental(rentalId);

        logger.debug("Аренда возобновлена: rentalId={}.", rentalId);
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
            @Parameter(description = "Идентификатор конечной точки проката", required = true) @Min(1) @RequestParam("endPointId") Long endPointId
    ) {
        logger.info("Полученный запрос: POST /end с параметрами: rentalId={}, endPointId={}.", rentalId, endPointId);
        this.rentalService.stopRental(rentalId, endPointId);

        logger.debug("Аренда окончена: rentalId={} на точке: endPointId={}.", rentalId, endPointId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    @Operation(
            summary = "Получение всех аренд.",
            description = "Скорее всего будет убрано в админ-панель."
    )
    public List<ShortRentalDto> getRentals() {
        logger.info("Полученный запрос: GET.");
        List<Rental> rentals = this.rentalService.getRentals();

        logger.debug("Получение всех аренд в количестве: {}.", rentals.size());
        return this.rentalMapper.mapToDtos(rentals);
    }
}
