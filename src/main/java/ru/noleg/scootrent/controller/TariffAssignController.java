package ru.noleg.scootrent.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.tariff.AssignTariffDto;
import ru.noleg.scootrent.dto.tariff.SubscribeUserDto;
import ru.noleg.scootrent.service.SubscriptionService;
import ru.noleg.scootrent.service.TariffAssignmentService;

// TODO есть ли смысл отдельный контроллер делать
@RestController
@RequestMapping("/api/tariffs/assign")
public class TariffAssignController {

    private final TariffAssignmentService tariffAssignmentService;
    private final SubscriptionService subscriptionService;


    public TariffAssignController(TariffAssignmentService tariffAssignmentService,
                                  SubscriptionService subscriptionService) {
        this.tariffAssignmentService = tariffAssignmentService;
        this.subscriptionService = subscriptionService;
    }


    @PostMapping("/{tariffId}")
    public ResponseEntity<Void> assignTariff(@PathVariable("tariffId") Long tariffId,
                                             @Valid @RequestBody AssignTariffDto tariffDto) {
        this.tariffAssignmentService.assignTariffToUser(
                tariffDto.userId(),
                tariffId,
                tariffDto.customPricePerUnit(),
                tariffDto.discountPct()
        );

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/subscribe/{tariffId}")
    // TODO как тут лучше передавать userId
    public ResponseEntity<Void> subscribeUserToTariff(@PathVariable("tariffId") Long tariffId,
                                                      @Valid @RequestBody SubscribeUserDto subscribeDto) {
        this.subscriptionService.subscribeUser(subscribeDto.userId(), tariffId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
