package ru.noleg.scootrent.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.tariff.TariffDto;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.mapper.TariffMapper;
import ru.noleg.scootrent.service.tariff.TariffService;

import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
public class TariffController {

    private final TariffService tariffService;
    private final TariffMapper tariffMapper;

    public TariffController(TariffService tariffService, TariffMapper tariffMapper) {
        this.tariffService = tariffService;
        this.tariffMapper = tariffMapper;
    }

    @PostMapping
    public ResponseEntity<Long> addTariff(@Valid @RequestBody TariffDto tariffDto) {
        Tariff tariff = this.tariffMapper.mapToEntity(tariffDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.tariffService.createTariff(tariff));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> disableTariff(@PathVariable("id") Long id) {
        tariffService.deactivateTariff(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TariffDto>> getActiveTariffs() {
        List<Tariff> tariffs = this.tariffService.getActiveTariffs();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.tariffMapper.mapToDetailDtos(tariffs));
    }
}
