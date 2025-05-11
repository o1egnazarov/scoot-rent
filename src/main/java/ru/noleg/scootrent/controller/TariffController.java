package ru.noleg.scootrent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.tariff.TariffDto;
import ru.noleg.scootrent.dto.tariff.UpdateTariffDto;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.mapper.TariffMapper;
import ru.noleg.scootrent.repository.TariffRepository;
import ru.noleg.scootrent.service.tariff.TariffService;

import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
@Validated
@Tag(
        name = "Контроллер для тарифов.",
        description = "Позволяет добавлять/отключать/получать тарифы."
)
public class TariffController {

    private static final Logger logger = LoggerFactory.getLogger(TariffController.class);

    private final TariffService tariffService;
    private final TariffMapper tariffMapper;
    private final TariffRepository tariffRepository;

    public TariffController(TariffService tariffService, TariffMapper tariffMapper, TariffRepository tariffRepository) {
        this.tariffService = tariffService;
        this.tariffMapper = tariffMapper;
        this.tariffRepository = tariffRepository;
    }

    @PostMapping
    @Operation(
            summary = "Добавление тарифа.",
            description = "Позволяет добавить новый тариф."
    )
    public ResponseEntity<Long> addTariff(@Valid @RequestBody TariffDto tariffDto) {
        logger.info("Полученный запрос: POST добавления тарифа: {}.", tariffDto.title());

        Tariff tariff = this.tariffMapper.mapToEntity(tariffDto);
        Long tariffId = this.tariffService.createTariff(tariff);

        logger.debug("Тариф добавлен с ID: {}.", tariffId);
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
        logger.info("Полученный запрос: PUT обновления тарифа с id: {}.", id);
        Tariff tariff = this.tariffService.getTariff(id);

        this.tariffMapper.updateTariffFromDto(updateTariffDto, tariff);
        this.tariffService.createTariff(tariff);

        logger.debug("Тариф с ID: {} успешно обновлен.", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.tariffMapper.mapToDto(tariff));
    }

    // TODO наверное добавить и активацию тарифа
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Деактивация тарифа.",
            description = "Позволяет деактивировать конкретный тариф."
    )
    public ResponseEntity<Void> disableTariff(
            @Parameter(description = "Идентификатор тарифа", required = true) @Min(1) @PathVariable("id") Long id
    ) {
        logger.info("Полученный запрос: DELETE деактивации тарифа с id: {}.", id);

        this.tariffService.deactivateTariff(id);

        logger.debug("Тариф с id: {}, успешно деактивирован.", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping
    @Operation(
            summary = "Получение всех активных тарифов.",
            description = "Позволяет получить всевозможные активные тарифы."
    )
    public ResponseEntity<List<TariffDto>> getActiveTariffs() {
        logger.info("Полученный запрос: GET получения всех активных тарифов.");

        List<Tariff> tariffs = this.tariffService.getActiveTariffs();

        logger.debug("Получено тарифов: {}.", tariffs.size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.tariffMapper.mapToDtos(tariffs));
    }
}
