package ru.noleg.scootrent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
import ru.noleg.scootrent.mapper.TariffMapper;
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
        Tariff tariff = this.tariffMapper.mapToEntity(tariffDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.tariffService.createTariff(tariff));
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
        Tariff tariff = this.tariffService.getTariff(id);

        this.tariffMapper.updateTariffFromDto(updateTariffDto, tariff);
        this.tariffService.createTariff(tariff);

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
        this.tariffService.deactivateTariff(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(
            summary = "Получение всех активных тарифов.",
            description = "Позволяет получить всевозможные активные тарифы."
    )
    public ResponseEntity<List<TariffDto>> getActiveTariffs() {
        List<Tariff> tariffs = this.tariffService.getActiveTariffs();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.tariffMapper.mapToDtos(tariffs));
    }
}
