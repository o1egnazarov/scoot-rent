package ru.noleg.scootrent.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.rentalPoint.RentalPointDto;
import ru.noleg.scootrent.dto.rentalPoint.UpdateRentalPointDto;
import ru.noleg.scootrent.entity.rental.RentalPoint;
import ru.noleg.scootrent.mapper.RentalPointMapper;
import ru.noleg.scootrent.service.RentalPointService;

import java.util.List;

@RestController
@RequestMapping("/api/rentalPoints")
public class RentalPointController {

    private final RentalPointMapper rentalPointMapper;
    private final RentalPointService rentalPointService;

    public RentalPointController(RentalPointMapper rentalPointMapper, RentalPointService rentalPointService) {
        this.rentalPointMapper = rentalPointMapper;
        this.rentalPointService = rentalPointService;
    }

    @PostMapping
    public ResponseEntity<Long> addRentalPoint(@Valid @RequestBody RentalPointDto rentalPointDto) {
        RentalPoint rentalPoint = this.rentalPointMapper.mapToEntity(rentalPointDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.rentalPointService.add(rentalPoint));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RentalPointDto> updateRentalPoint(@PathVariable("id") Long id,
                                                            @Valid @RequestBody UpdateRentalPointDto rentalPointDto) {

        RentalPoint rentalPoint = this.rentalPointService.getRentalPoint(id);

        this.rentalPointMapper.updateRentalPointFromDto(rentalPointDto, rentalPoint);
        this.rentalPointService.add(rentalPoint);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.rentalPointMapper.mapToDto(rentalPoint));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRentalPoint(@PathVariable("id") Long id) {
        this.rentalPointService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<RentalPointDto>> getAllRentalPoints() {
        List<RentalPointDto> rentalPointDtos = this.rentalPointMapper.mapToDtos(this.rentalPointService.getAllRentalPoints());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(rentalPointDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalPointDto> getRentalPointById(@PathVariable("id") Long id) {
        RentalPoint rentalPoint = this.rentalPointService.getRentalPoint(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.rentalPointMapper.mapToDto(rentalPoint));
    }

    @GetMapping("/coordinates")
    public ResponseEntity<RentalPointDto> getRentalPointByCoordinates(@RequestParam("latitude") double latitude,
                                                                      @RequestParam("longitude") double longitude) {
        RentalPoint rentalPoint = this.rentalPointService.getRentalPointByCoordinates(latitude, longitude);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.rentalPointMapper.mapToDto(rentalPoint));
    }
}
