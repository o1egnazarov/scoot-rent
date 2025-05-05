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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.location.CreateLocationDto;
import ru.noleg.scootrent.dto.location.DetailLocationDto;
import ru.noleg.scootrent.dto.location.LocationDto;
import ru.noleg.scootrent.dto.location.UpdateLocationDto;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.mapper.LocationMapper;
import ru.noleg.scootrent.service.LocationService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
@Validated
@Tag(
        name = "Контроллер для локаций.",
        description = "Позволяет управлять иерархией локаций (обновлять/удалять/получать)."
)
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    private final LocationMapper locationMapper;
    private final LocationService locationService;

    public LocationController(LocationMapper locationMapper, LocationService locationService) {
        this.locationMapper = locationMapper;
        this.locationService = locationService;
    }

    @PostMapping
    @Operation(
            summary = "Добавление локации.",
            description = "Позволяет сохранить новую локацию."
    )
    public ResponseEntity<Long> addLocation(@Valid @RequestBody CreateLocationDto createLocationDto) {
        logger.info("Полученный запрос: POST добавления локации с адресом: {}.", createLocationDto.address());

        LocationNode locationNode = this.locationMapper.mapToEntity(createLocationDto);
        Long locationId = this.locationService.add(locationNode);

        logger.debug("Локация добавлена с ID: {}.", locationId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(locationId);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновление локации.",
            description = "Позволяет изменить конкретную локацию."
    )
    public ResponseEntity<DetailLocationDto> updateLocation(
            @Parameter(description = "Идентификатор локации", required = true) @Min(1) @PathVariable("id") Long id,
            @Valid @RequestBody UpdateLocationDto rentalPointDto
    ) {
        logger.info("Полученный запрос: PUT обновления локации с id: {}.", id);
        LocationNode locationNode = this.locationService.getLocationById(id);

        this.locationMapper.updateRentalPointFromDto(rentalPointDto, locationNode);
        this.locationService.add(locationNode);

        logger.debug("Локация с ID: {} успешно обновлена.", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.locationMapper.mapToDetailDto(locationNode));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление локации.",
            description = "Позволяет удалить конкретную локацию."
    )
    public ResponseEntity<Void> deleteLocation(
            @Parameter(description = "Идентификатор локации", required = true) @Min(1) @PathVariable("id") Long id
    ) {
        logger.info("Полученный запрос: DELETE удаления локации с id: {}.", id);

        this.locationService.delete(id);

        logger.debug("Локация с id: {}, успешно удалена.", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping
    @Operation(
            summary = "Получение всех локаций.",
            description = "Позволяет получить всевозможные локации системы."
    )
    public ResponseEntity<List<LocationDto>> getAllLocation() {
        logger.info("Полученный запрос: GET получения всех локаций.");

        List<LocationNode> allLocations = this.locationService.getAllLocations();
        List<LocationDto> locations =
                this.locationMapper.mapToDtos(allLocations);

        logger.debug("Все локации успешно получены.");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(locations);
    }

    @GetMapping("/{id}/children")
    @Operation(
            summary = "Получение дочерних узлов.",
            description = "Позволяет получить дочерние узлы конкретной локации."
    )
    public ResponseEntity<List<DetailLocationDto>> getChildrenLocation(
            @Parameter(description = "Идентификатор локации", required = true) @Min(1) @PathVariable("id") Long id
    ) {
        logger.info("Полученный запрос: GET получения дочерних узлов локации с id: {}.", id);

        List<LocationNode> locationNode = this.locationService.getChildrenLocation(id);

        logger.debug("Получена локация с id: {} с дочерними узлами.", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.locationMapper.mapToDetailDtos(locationNode));
    }

    // TODO как лучше? возвращать любую локацию по id или проверять что переданный id это id локации с типом RENTAL_POINT,
    // TODO иначе кидать BAD REQUEST
    @GetMapping("/{id}")
    @Operation(
            summary = "Получение конкретной локации по id.",
            description = "Позволяет получить локацию по id."
    )
    public ResponseEntity<DetailLocationDto> getLocationById(
            @Parameter(description = "Идентификатор локации", required = true) @Min(1) @PathVariable("id") Long id
    ) {
        logger.info("Полученный запрос: GET получения локации с id: {}.", id);

        LocationNode locationNode = this.locationService.getLocationById(id);

        logger.debug("Получена локация id: {}.", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.locationMapper.mapToDetailDto(locationNode));
    }

    @GetMapping("/coordinates")
    @Operation(
            summary = "Получение конкретной локации по координатам.",
            description = "Позволяет получить локацию и все ее дочерние точки (если такие есть) по широте и долготе."
    )
    public ResponseEntity<DetailLocationDto> getLocationByCoordinates(
            @Parameter(description = "Широта", required = true) @RequestParam("latitude") BigDecimal latitude,
            @Parameter(description = "Долгота", required = true) @RequestParam("longitude") BigDecimal longitude
    ) {
        logger.info("Полученный запрос: GET с параметрами: latitude={}, longitude={}.", latitude, longitude);

        LocationNode locationNode = this.locationService.getLocationByCoordinates(latitude, longitude);

        logger.debug("Получена локация по координатам с id: {}.", locationNode.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.locationMapper.mapToDetailDto(locationNode));
    }
}
