package ru.noleg.scootrent.controller.scooter;

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
import ru.noleg.scootrent.service.scooter.ScooterService;
import ru.noleg.scootrent.service.scooter.UpdateScooterCommand;

import java.util.List;

@PreAuthorize("hasRole('MODERATOR')")
@RestController
@RequestMapping("/api/scooters")
@Validated
@Tag(
        name = "Контроллер для самокатов.",
        description = "Позволяет создавать/обновлять/удалять/получать самокаты."
)
@SecurityRequirement(name = "JWT")
public class ScooterController {

    private static final Logger logger = LoggerFactory.getLogger(ScooterController.class);

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
        logger.info("Request: POST added scooter with number plate: {}.", scooterDto.numberPlate());

        Scooter scooter = this.scooterMapper.mapToEntity(scooterDto);
        Long scooterId = this.scooterService.add(scooter);

        logger.info("Scooter added with id: {}.", scooterId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(scooterId);
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
        logger.info("Request: PUT update scooter with id: {}.", id);

        var command = new UpdateScooterCommand(
                scooterDto.numberPlate(),
                scooterDto.status(),
                scooterDto.durationInUsed(),
                scooterDto.modelId(),
                scooterDto.rentalPointId()
        );
        Scooter scooter = this.scooterService.update(id, command);
        logger.info("Scooter with id: {} successfully updated.", id);

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
        logger.info("Request: DELETE delete scooter with id: {}.", id);

        this.scooterService.delete(id);

        logger.info("Scooter with id: {}, successfully deleted.", id);
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
        logger.info("Request: GET fetch all scooters.");

        List<ScooterDto> scooterDtos = this.scooterMapper.mapToDtos(this.scooterService.getAllScooters());

        logger.info("Got {} scooters.", scooterDtos.size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(scooterDtos);
    }
}
