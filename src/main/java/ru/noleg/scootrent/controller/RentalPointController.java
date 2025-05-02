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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.rentalPoint.CreateRentalPointDto;
import ru.noleg.scootrent.dto.rentalPoint.DetailRentalPointDto;
import ru.noleg.scootrent.dto.rentalPoint.RentalPointDto;
import ru.noleg.scootrent.dto.rentalPoint.UpdateRentalPointDto;
import ru.noleg.scootrent.entity.rental.RentalPoint;
import ru.noleg.scootrent.mapper.RentalPointMapper;
import ru.noleg.scootrent.service.RentalPointService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/rentalPoints")
@Validated
@Tag(
        name = "Контроллер для точки проката.",
        description = "Позволяет создавать/обновлять/удалять/получать точки проката."
)
public class RentalPointController {

    private final RentalPointMapper rentalPointMapper;
    private final RentalPointService rentalPointService;

    public RentalPointController(RentalPointMapper rentalPointMapper, RentalPointService rentalPointService) {
        this.rentalPointMapper = rentalPointMapper;
        this.rentalPointService = rentalPointService;
    }

    @PostMapping
    @Operation(
            summary = "Добавление точки проката.",
            description = "Позволяет сохранить новую точку проката."
    )
    public ResponseEntity<Long> addRentalPoint(@Valid @RequestBody CreateRentalPointDto createRentalPointDto) {
        RentalPoint rentalPoint = this.rentalPointMapper.mapToEntity(createRentalPointDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.rentalPointService.add(rentalPoint));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновление токи проката.",
            description = "Позволяет изменить конкретную точку проката."
    )
    public ResponseEntity<DetailRentalPointDto> updateRentalPoint(
            @Parameter(description = "Идентификатор точки проката", required = true) @Min(1) @PathVariable("id") Long id,
            @Valid @RequestBody UpdateRentalPointDto rentalPointDto
    ) {
        RentalPoint rentalPoint = this.rentalPointService.getRentalPoint(id);

        this.rentalPointMapper.updateRentalPointFromDto(rentalPointDto, rentalPoint);
        this.rentalPointService.add(rentalPoint);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.rentalPointMapper.mapToDetailDto(rentalPoint));
    }

    // TODO если удалить родительскую что будет
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление точки проката.",
            description = "Позволяет удалить конкретную точку проката."
    )
    public ResponseEntity<Void> deleteRentalPoint(
            @Parameter(description = "Идентификатор точки проката", required = true) @Min(1) @PathVariable("id") Long id
    ) {
        this.rentalPointService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping
    @Operation(
            summary = "Получение всех точек проката.",
            description = "Позволяет получить всевозможные точки проката системы."
    )
    public ResponseEntity<List<RentalPointDto>> getAllRentalPoints() {
        List<RentalPointDto> detailRentalPointDtos =
                this.rentalPointMapper.mapToDtos(this.rentalPointService.getAllRentalPoints());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(detailRentalPointDtos);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получение конкретной точки проката по id.",
            description = "Позволяет получить точки проката и все ее дочерние точки (если такие есть) по id."
    )
    public ResponseEntity<DetailRentalPointDto> getRentalPointById(
            @Parameter(description = "Идентификатор точки проката", required = true) @Min(1) @PathVariable("id") Long id
    ) {
        RentalPoint rentalPoint = this.rentalPointService.getRentalPoint(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.rentalPointMapper.mapToDetailDto(rentalPoint));
    }

    @GetMapping("/coordinates")
    @Operation(
            summary = "Получение конкретной точки проката по координатам.",
            description = "Позволяет получить точки проката и все ее дочерние точки (если такие есть) по широте и долготе."
    )
    public ResponseEntity<DetailRentalPointDto> getRentalPointByCoordinates(
            @Parameter(description = "Широта", required = true) @RequestParam("latitude") BigDecimal latitude,
            @Parameter(description = "Долгота", required = true) @RequestParam("longitude") BigDecimal longitude
    ) {
        RentalPoint rentalPoint = this.rentalPointService.getRentalPointByCoordinates(latitude, longitude);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.rentalPointMapper.mapToDetailDto(rentalPoint));
    }
}
