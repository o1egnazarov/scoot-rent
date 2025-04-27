package ru.noleg.scootrent.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.rental.ShortRentalDto;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.mapper.RentalMapper;
import ru.noleg.scootrent.service.RentalService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/scooters")
public class AdminScooterController {

    private final RentalService rentalService;
    private final RentalMapper rentalMapper;

    public AdminScooterController(RentalService rentalService, RentalMapper rentalMapper) {
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
    }

    @GetMapping("/{scooterId}/history")
    public ResponseEntity<List<ShortRentalDto>> getRentalHistory(@PathVariable Long scooterId) {
        List<Rental> rentalHistory = rentalService.getRentalHistoryForScooter(scooterId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.rentalMapper.mapToShortDtos(rentalHistory));
    }
}