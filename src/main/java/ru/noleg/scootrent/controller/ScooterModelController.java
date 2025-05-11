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
import ru.noleg.scootrent.dto.scooter.scootermodel.ScooterModelDto;
import ru.noleg.scootrent.dto.scooter.scootermodel.UpdateScooterModelDto;
import ru.noleg.scootrent.entity.scooter.ScooterModel;
import ru.noleg.scootrent.mapper.ScooterModelMapper;
import ru.noleg.scootrent.service.scooter.ScooterModelService;

import java.util.List;

@RestController
@RequestMapping("/api/scooters/models")
@Validated
@Tag(
        name = "Контроллер для моделей самокатов.",
        description = "Позволяет создавать/обновлять/удалять/получать модели самокатов."
)
public class ScooterModelController {

    private static final Logger logger = LoggerFactory.getLogger(ScooterModelController.class);

    private final ScooterModelMapper scooterModelMapper;
    private final ScooterModelService scooterModelService;

    public ScooterModelController(ScooterModelMapper scooterModelMapper, ScooterModelService scooterModelService) {
        this.scooterModelMapper = scooterModelMapper;
        this.scooterModelService = scooterModelService;
    }

    @PostMapping
    @Operation(
            summary = "Добавление модели самоката.",
            description = "Позволяет сохранить новую модель самоката."
    )
    public ResponseEntity<Long> addScooterModel(@Valid @RequestBody ScooterModelDto scooterModelDto) {
        logger.info("Полученный запрос: POST добавления модели самоката: {}.", scooterModelDto.title());

        ScooterModel scooterModel = this.scooterModelMapper.mapToEntity(scooterModelDto);
        Long scooterModelId = this.scooterModelService.add(scooterModel);

        logger.debug("Модель самоката добавлена с ID: {}.", scooterModelId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(scooterModelId);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновление модели самоката.",
            description = "Позволяет изменить конкретную модель самоката."
    )
    public ResponseEntity<ScooterModelDto> updateScooterModel(
            @Parameter(description = "Идентификатор модели самоката", required = true) @Min(1) @PathVariable("id") Long id,
            @Valid @RequestBody UpdateScooterModelDto scooterModelDto
    ) {
        logger.info("Полученный запрос: PUT обновление модели самоката с id: {}.", id);

        ScooterModel scooterModel = this.scooterModelService.getScooterModel(id);

        this.scooterModelMapper.updateScooterModelFromDto(scooterModelDto, scooterModel);
        this.scooterModelService.add(scooterModel);

        logger.debug("Модель самоката с ID: {} успешно обновлена.", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.scooterModelMapper.mapToDto(scooterModel));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление модели самоката.",
            description = "Позволяет удалить конкретную модель самоката."
    )
    public ResponseEntity<Void> deleteScooter(
            @Parameter(description = "Идентификатор модели самоката", required = true) @Min(1) @PathVariable("id") Long id
    ) {
        logger.info("Полученный запрос: DELETE удаления модели самоката с id: {}.", id);

        this.scooterModelService.delete(id);

        logger.debug("Модель самоката с id: {}, успешно удалена.", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping
    @Operation(
            summary = "Получение всех моделей самокатов.",
            description = "Позволяет получить всевозможные модели самокатов в системе."
    )
    public ResponseEntity<List<ScooterModelDto>> getAllScooterModels() {
        logger.info("Полученный запрос: GET получения всех моделей самоката.");

        List<ScooterModelDto> scooterModelDtos =
                this.scooterModelMapper.mapToDtos(this.scooterModelService.getAllScooterModels());

        logger.debug("Получено моделей самокатов: {}.", scooterModelDtos.size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(scooterModelDtos);
    }
}
