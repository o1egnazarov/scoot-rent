package ru.noleg.scootrent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.rental.ShortRentalDto;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.mapper.RentalMapper;
import ru.noleg.scootrent.service.rental.RentalService;
import ru.noleg.scootrent.service.user.UserDetailsImpl;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@Validated
@Tag(
        name = "Контроллер для аренды самокатов.",
        description = "Позволяет начать/приостановить/закончить аренду."
)
@SecurityRequirement(name = "JWT")
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> startRental(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "Идентификатор самоката", required = true) @Min(1) @RequestParam("scooterId")
            Long scooterId,
            @Parameter(description = "Идентификатор стартовой точки аренды", required = true) @Min(1) @RequestParam("startPointId")
            Long startPointId,
            @Parameter(description = "Выбор оплаты почасовая/поминутная", required = true) @NotNull @RequestParam("billingMode")
            BillingMode billingMode
    ) {
        Long userId = userDetails.getId();
        logger.info("Request: POST /start with param: userId={}, scooterId={}, rentalPointId={}, billingMode={} ",
                userId, scooterId, startPointId, billingMode);
        Long rentalId = this.rentalService.startRental(userId, scooterId, startPointId, billingMode);

        logger.info("Rental with id: {} successfully started.", rentalId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rentalId);
    }

    @PostMapping("/pause")
    @Operation(
            summary = "Приостановление аренды.",
            description = "Позволяет поставить конкретную аренду на паузу."
    )
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> pauseRental(
            @Parameter(description = "Идентификатор аренды", required = true) @Min(1) @RequestParam("rentalId") Long rentalId
    ) {
        logger.info("Request: POST /pause с параметрами: rentalId={}.", rentalId);
        this.rentalService.pauseRental(rentalId);

        logger.info("Rental with id: {} paused.", rentalId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/resume")
    @Operation(
            summary = "Возобновление аренды.",
            description = "Позволяет возобновить конкретную аренду."
    )
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> resumeRental(
            @Parameter(description = "Идентификатор аренды", required = true) @Min(1) @RequestParam("rentalId") Long rentalId
    ) {
        logger.info("Request: POST /resume with param: rentalId={}.", rentalId);
        this.rentalService.resumeRental(rentalId);

        logger.info("Rental with id: {} resumed.", rentalId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/end")
    @Operation(
            summary = "Окончание аренды.",
            description = "Позволяет закончить конкретную аренду на конкретной точке проката."
    )
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> endRental(
            @Parameter(description = "Идентификатор аренды", required = true) @Min(1) @RequestParam("rentalId") Long rentalId,
            @Parameter(description = "Идентификатор конечной точки проката", required = true) @Min(1) @RequestParam("endPointId") Long endPointId
    ) {
        logger.info("Request: POST /end with param: rentalId={}, endPointId={}.", rentalId, endPointId);
        this.rentalService.stopRental(rentalId, endPointId);

        logger.info("Rental with id: {} stooped on rental point with id: {}.", rentalId, endPointId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    @Operation(
            summary = "Получение всех аренд.",
            description = "Получает все активные/завершенные аренды."
    )
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<List<ShortRentalDto>> getRentals() {
        logger.info("Request: GET.");
        List<Rental> rentals = this.rentalService.getRentals();

        logger.info("Got {} rentals.", rentals.size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.rentalMapper.mapToDtos(rentals));
    }
}
