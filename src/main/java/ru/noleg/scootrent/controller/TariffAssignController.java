package ru.noleg.scootrent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.tariff.AssignTariffDto;
import ru.noleg.scootrent.dto.tariff.SubscribeUserDto;
import ru.noleg.scootrent.service.tariff.SubscriptionService;
import ru.noleg.scootrent.service.tariff.TariffAssignmentService;

@RestController
@RequestMapping("/api/tariffs/assign")
@Validated
@Tag(
        name = "Контроллер для назначения специального тарифа/подписки.",
        description = "Позволяет назначать специальный тариф/подписку для пользователя."
)
public class TariffAssignController {

    private final TariffAssignmentService tariffAssignmentService;
    private final SubscriptionService subscriptionService;


    public TariffAssignController(TariffAssignmentService tariffAssignmentService,
                                  SubscriptionService subscriptionService) {
        this.tariffAssignmentService = tariffAssignmentService;
        this.subscriptionService = subscriptionService;
    }


    @PostMapping("/{tariffId}")
    @Operation(
            summary = "Назначение специального тарифа.",
            description = "Позволяет назначить специальный тариф пользователю."
    )
    public ResponseEntity<Void> assignTariff(
            @Parameter(description = "Идентификатор тарифа", required = true) @Min(1) @PathVariable("tariffId") Long tariffId,
            @Valid @RequestBody AssignTariffDto tariffDto
    ) {
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
    @Operation(
            summary = "Назначение подписки.",
            description = "Позволяет назначить подписку пользователю."
    )
    public ResponseEntity<Void> subscribeUserToTariff(
            @Parameter(description = "Идентификатор тарифа", required = true) @Min(1) @PathVariable("tariffId") Long tariffId,
            @Valid @RequestBody SubscribeUserDto subscribeDto
    ) {
        this.subscriptionService.subscribeUser(subscribeDto.userId(), tariffId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
