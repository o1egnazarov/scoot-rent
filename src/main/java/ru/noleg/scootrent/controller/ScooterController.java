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
import ru.noleg.scootrent.dto.scooter.ScooterDto;
import ru.noleg.scootrent.dto.scooter.UpdateScooterDto;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.mapper.ScooterMapper;
import ru.noleg.scootrent.service.ScooterService;

import java.util.List;

@RestController
@RequestMapping("/api/scooters")
@Validated
@Tag(
        name = "Контроллер для самокатов.",
        description = "Позволяет создавать/обновлять/удалять/получать самокаты."
)
public class ScooterController {

    private final ScooterMapper scooterMapper;
    private final ScooterService scooterService;

    public ScooterController(ScooterMapper scooterMapper, ScooterService scooterService) {
        this.scooterMapper = scooterMapper;
        this.scooterService = scooterService;
    }

    @PostMapping
    @Operation(
            summary = "Добавление самоката.",
            description = "Позволяет сохранить новый самокат."
    )
    public ResponseEntity<Long> addScooter(@Valid @RequestBody ScooterDto scooterDto) {
        Scooter scooter = this.scooterMapper.mapToEntity(scooterDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.scooterService.add(scooter));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновление самоката.",
            description = "Позволяет изменить конкретный самокат."
    )
    public ResponseEntity<ScooterDto> updateScooter(
            @Parameter(description = "Идентификатор самоката", required = true) @Min(1) @PathVariable("id") Long id,
            @Valid @RequestBody UpdateScooterDto scooterDto
    ) {
        Scooter scooter = this.scooterService.getScooter(id);

        this.scooterMapper.updateScooterFromDto(scooterDto, scooter);
        this.scooterService.add(scooter);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.scooterMapper.mapToDto(scooter));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление самоката.",
            description = "Позволяет удалить конкретный самокат."
    )
    public ResponseEntity<Void> deleteScooter(
            @Parameter(description = "Идентификатор самоката", required = true) @Min(1) @PathVariable("id") Long id
    ) {
        this.scooterService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping
    @Operation(
            summary = "Получение всех самокатов.",
            description = "Позволяет получить всевозможные самокаты в системе."
    )
    public ResponseEntity<List<ScooterDto>> getAllScooters() {
        List<ScooterDto> scooterDtos = this.scooterMapper.mapToDtos(this.scooterService.getAllScooters());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(scooterDtos);
    }
}
