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
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.scooter.ScooterDto;
import ru.noleg.scootrent.dto.scooter.UpdateScooterDto;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.mapper.ScooterMapper;
import ru.noleg.scootrent.service.ScooterService;

import java.util.List;

@RestController
@RequestMapping("/api/scooters")
public class ScooterController {

    private final ScooterMapper scooterMapper;
    private final ScooterService scooterService;

    public ScooterController(ScooterMapper scooterMapper, ScooterService scooterService) {
        this.scooterMapper = scooterMapper;
        this.scooterService = scooterService;
    }

    @PostMapping
    public ResponseEntity<Long> addScooter(@Valid @RequestBody ScooterDto scooterDto) {
        Scooter scooter = this.scooterMapper.mapToEntity(scooterDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.scooterService.add(scooter));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScooterDto> updateScooter(@PathVariable("id") Long id,
                                                    @Valid @RequestBody UpdateScooterDto scooterDto) {
        Scooter scooter = this.scooterService.getScooter(id);

        this.scooterMapper.updateScooterFromDto(scooterDto, scooter);
        this.scooterService.add(scooter);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.scooterMapper.mapToDetailDto(scooter));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScooter(@PathVariable("id") Long id) {
        this.scooterService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<ScooterDto>> getAllScooters() {
        List<ScooterDto> scooterDtos = this.scooterMapper.mapToDetailDtos(this.scooterService.getAllScooters());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(scooterDtos);
    }
}
