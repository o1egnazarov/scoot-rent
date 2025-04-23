package ru.noleg.scootrent.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.rental.RentalDto;
import ru.noleg.scootrent.dto.rental.ShortRentalDto;
import ru.noleg.scootrent.mapper.RentalMapper;
import ru.noleg.scootrent.service.RentalService;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final RentalMapper rentalMapper;

    public RentalController(RentalService rentalService, RentalMapper rentalMapper) {
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
    }

    @PostMapping("/start")
    public ResponseEntity<Long> startRental(@RequestParam("userId") Long userId,
                                            @RequestParam("scooterId") Long scooterId,
                                            @RequestParam("rentalPointId") Long rentalPointId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.rentalService.startRental(userId, scooterId, rentalPointId));
    }

    @PostMapping("/end")
    public ResponseEntity<Void> endRental(@RequestParam("rentalId") Long rentalId,
                                           @RequestParam("rentalPointId") Long rentalPointId) {

        this.rentalService.stopRental(rentalId, rentalPointId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public List<ShortRentalDto> getRentals() {
        return this.rentalMapper.mapToShorDtos(this.rentalService.getRentals());
    }
}
