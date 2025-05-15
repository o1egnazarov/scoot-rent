package ru.noleg.scootrent.controller.rolebased;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.rental.ScooterRentalHistoryDto;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.mapper.RentalHistoryMapper;
import ru.noleg.scootrent.service.rental.RentalService;

import java.util.List;

@RestController
@RequestMapping("/api/moderator")
@Validated
@Tag(
        name = "Контроллер модератора.",
        description = "Управляет сущностями системы."
)
@SecurityRequirement(name = "JWT")
public class ModeratorController {

    private static final Logger logger = LoggerFactory.getLogger(ModeratorController.class);

    private final RentalService rentalService;
    private final RentalHistoryMapper rentalHistoryMapper;

    public ModeratorController(RentalService rentalService, RentalHistoryMapper rentalHistoryMapper) {
        this.rentalService = rentalService;
        this.rentalHistoryMapper = rentalHistoryMapper;
    }

    @GetMapping("/{scooterId}/history")
    @Operation(
            summary = "История аренды самоката.",
            description = "Позволяет получить историю аренды конкретного самоката."
    )
    public ResponseEntity<List<ScooterRentalHistoryDto>> getRentalHistory(
            @Parameter(description = "Идентификатор самоката", required = true)
            @PathVariable("scooterId") @Min(1) Long scooterId
    ) {
        logger.info("Request: GET /{}/history.", scooterId);
        List<Rental> rentalHistory = this.rentalService.getRentalHistoryForScooter(scooterId);

        logger.debug("{} rental history records received for scooter with id: {}.", rentalHistory.size(), scooterId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.rentalHistoryMapper.mapToScooterRentalDtos(rentalHistory));
    }
}