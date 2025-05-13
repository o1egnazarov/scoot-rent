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
import ru.noleg.scootrent.entity.location.LocationType;
import ru.noleg.scootrent.mapper.LocationMapper;
import ru.noleg.scootrent.service.location.LocationService;

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
        logger.info("Request: POST add location with address: {}.", createLocationDto.address());

        LocationNode locationNode = this.locationMapper.mapToEntity(createLocationDto);
        Long locationId = this.locationService.add(locationNode);

        logger.info("Location added with id: {}.", locationId);
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
            @Valid @RequestBody UpdateLocationDto locationDto
    ) {
        logger.info("Request: PUT update location with id: {}.", id);
        LocationNode locationNode = this.locationService.getLocationById(id);

        this.locationMapper.updateRentalPointFromDto(locationDto, locationNode);
        this.locationService.add(locationNode);

        logger.info("Location with id: {} has been successfully updated.", id);
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
        logger.info("Request: DELETE delete location with id: {}.", id);

        this.locationService.delete(id);

        logger.info("Location with id: {}, has been successfully deleted.", id);
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
        logger.info("Request: GET get all locations.");

        List<LocationNode> allLocations = this.locationService.getAllLocations();
        List<LocationDto> locations =
                this.locationMapper.mapToDtos(allLocations);

        logger.info("All locations has been successfully retrieved.");
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
        logger.info("Request: GET getting child location nodes with id: {}.", id);

        List<LocationNode> locationNode = this.locationService.getChildrenLocation(id);

        logger.info("Received location with id: {} with child nodes.", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.locationMapper.mapToDetailDtos(locationNode));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получение точки проката по id.",
            description = "Возвращает локацию с типом RENTAL_POINT по её id."
    )
    public ResponseEntity<DetailLocationDto> getRentalPointById(
            @Parameter(description = "Идентификатор точки проката", required = true) @Min(1) @PathVariable("id") Long id
    ) {
        logger.info("Request: GET get rental point with id: {}.", id);

        LocationNode locationNode = this.locationService.getLocationByIdAndType(id, LocationType.RENTAL_POINT);

        logger.info("Received rental point with id: {}.", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.locationMapper.mapToDetailDto(locationNode));
    }

    @GetMapping("/coordinates")
    @Operation(
            summary = "Получение точки проката по координатам.",
            description = "Возвращает локацию с типом RENTAL_POINT по широте и долготе."
    )
    public ResponseEntity<DetailLocationDto> getRentalPointByCoordinates(
            @Parameter(description = "Широта", required = true) @RequestParam("latitude") BigDecimal latitude,
            @Parameter(description = "Долгота", required = true) @RequestParam("longitude") BigDecimal longitude
    ) {
        logger.info("Request: GET with param: latitude={}, longitude={}.", latitude, longitude);

        LocationNode locationNode = this.locationService.getLocationByCoordinatesAndType(latitude, longitude, LocationType.RENTAL_POINT);

        logger.info("Received rental point by coordinates with id: {}.", locationNode.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.locationMapper.mapToDetailDto(locationNode));
    }
}
