package ru.noleg.scootrent.controller.tariff;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.tariff.TariffDto;
import ru.noleg.scootrent.dto.tariff.UpdateTariffDto;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.mapper.TariffMapper;
import ru.noleg.scootrent.service.tariff.TariffService;

import java.util.List;

@PreAuthorize("hasRole('MODERATOR')")
@RestController
@RequestMapping("/api/tariffs")
@Validated
@Tag(
        name = "Контроллер для тарифов.",
        description = "Позволяет добавлять/отключать/получать тарифы."
)
@SecurityRequirement(name = "JWT")
public class TariffController {

    private static final Logger logger = LoggerFactory.getLogger(TariffController.class);

    private final TariffService tariffService;
    private final TariffMapper tariffMapper;

    public TariffController(TariffService tariffService, TariffMapper tariffMapper) {
        this.tariffService = tariffService;
        this.tariffMapper = tariffMapper;
    }

    @PostMapping
    @Operation(
            summary = "Добавление тарифа.",
            description = "Позволяет добавить новый тариф."
    )
    public ResponseEntity<Long> addTariff(@Valid @RequestBody TariffDto tariffDto) {
        logger.info("Request: POST added tariff: {}.", tariffDto.title());

        Tariff tariff = this.tariffMapper.mapToEntity(tariffDto);
        Long tariffId = this.tariffService.createTariff(tariff);

        logger.info("Tariff with id: {} added.", tariffId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(tariffId);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновление тарифа.",
            description = "Позволяет изменить конкретный тариф."
    )
    public ResponseEntity<TariffDto> updateTariff(
            @Parameter(description = "Идентификатор тарифа", required = true) @Min(1) @PathVariable("id") Long id,
            @Valid @RequestBody UpdateTariffDto updateTariffDto
    ) {
        logger.info("Request: PUT update tariff with id: {}.", id);
        Tariff tariff = this.tariffService.getTariff(id);

        this.tariffMapper.updateTariffFromDto(updateTariffDto, tariff);
        this.tariffService.createTariff(tariff);

        logger.info("Tariff with id: {} successfully updated.", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.tariffMapper.mapToDto(tariff));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(
            summary = "Деактивация тарифа.",
            description = "Позволяет деактивировать конкретный тариф."
    )
    public ResponseEntity<Void> deactivateTariff(
            @Parameter(description = "Идентификатор тарифа", required = true) @Min(1) @PathVariable("id") Long id
    ) {
        logger.info("Request: PATCH /deactivate deactivate tariff with id: {}.", id);

        this.tariffService.deactivateTariff(id);

        logger.info("Tariff with id: {}, deactivate.", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PatchMapping("/{id}/activate")
    @Operation(
            summary = "Активация тарифа.",
            description = "Позволяет активировать ранее деактивированный тариф."
    )
    public ResponseEntity<Void> activateTariff(
            @Parameter(description = "Идентификатор тарифа", required = true) @Min(1) @PathVariable("id") Long id
    ) {
        logger.info("Request: PATCH activate tariff with id: {}.", id);

        this.tariffService.activateTariff(id);

        logger.info("Tariff with id: {} activated.", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/active")
    @Operation(
            summary = "Получение всех активных тарифов.",
            description = "Позволяет получить всевозможные активные тарифы."
    )
    public ResponseEntity<List<TariffDto>> getActiveTariffs() {
        logger.info("Request: GET /active fetch all active tariff.");

        List<Tariff> tariffs = this.tariffService.getActiveTariffs();

        logger.info("Got {} active tariffs.", tariffs.size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.tariffMapper.mapToDtos(tariffs));
    }

    @GetMapping
    @Operation(
            summary = "Получение всех тарифов.",
            description = "Позволяет получить всевозможные тарифы."
    )
    public ResponseEntity<List<TariffDto>> getTariffs() {
        logger.info("Request: GET fetch all tariff.");

        List<Tariff> tariffs = this.tariffService.getAllTariffs();

        logger.info("Got {} all tariff.", tariffs.size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.tariffMapper.mapToDtos(tariffs));
    }
}
