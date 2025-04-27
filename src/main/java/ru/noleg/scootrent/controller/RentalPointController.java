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
import ru.noleg.scootrent.dto.rentalPoint.CreateRentalPointDto;
import ru.noleg.scootrent.dto.rentalPoint.DetailRentalPointDto;
import ru.noleg.scootrent.dto.rentalPoint.UpdateRentalPointDto;
import ru.noleg.scootrent.entity.rental.RentalPoint;
import ru.noleg.scootrent.mapper.RentalPointMapper;
import ru.noleg.scootrent.service.RentalPointService;

import java.math.BigDecimal;
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
    public ResponseEntity<Long> addRentalPoint(@Valid @RequestBody CreateRentalPointDto createRentalPointDto) {
        RentalPoint rentalPoint = this.rentalPointMapper.mapToEntity(createRentalPointDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.rentalPointService.add(rentalPoint));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetailRentalPointDto> updateRentalPoint(@PathVariable("id") Long id,
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
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping
    // TODO доработать чтобы не дублировалось
    public ResponseEntity<List<DetailRentalPointDto>> getAllRentalPoints() {
        List<DetailRentalPointDto> detailRentalPointDtos = this.rentalPointMapper.mapToDtos(this.rentalPointService.getAllRentalPoints());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(detailRentalPointDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailRentalPointDto> getRentalPointById(@PathVariable("id") Long id) {
        RentalPoint rentalPoint = this.rentalPointService.getRentalPoint(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.rentalPointMapper.mapToDto(rentalPoint));
    }

    @GetMapping("/coordinates")
    public ResponseEntity<DetailRentalPointDto> getRentalPointByCoordinates(@RequestParam("latitude") BigDecimal latitude,
                                                                            @RequestParam("longitude") BigDecimal longitude) {
        RentalPoint rentalPoint = this.rentalPointService.getRentalPointByCoordinates(latitude, longitude);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.rentalPointMapper.mapToDto(rentalPoint));
    }
}
