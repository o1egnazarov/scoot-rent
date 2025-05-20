package ru.noleg.scootrent.controller.tariff;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.tariff.AssignTariffDto;
import ru.noleg.scootrent.dto.tariff.UserTariffDto;
import ru.noleg.scootrent.entity.tariff.UserTariff;
import ru.noleg.scootrent.mapper.UserTariffMapper;
import ru.noleg.scootrent.service.user.UserDetailsImpl;
import ru.noleg.scootrent.service.tariff.TariffAssignmentService;

@RestController
@RequestMapping("/api/user-tariffs")
@Validated
@Tag(
        name = "Контроллер для назначения специального тарифа.",
        description = "Позволяет назначать и управлять пользовательскими тарифами."
)
@SecurityRequirement(name = "JWT")
public class UserTariffController {

    private static final Logger logger = LoggerFactory.getLogger(UserTariffController.class);

    private final TariffAssignmentService tariffAssignmentService;
    private final UserTariffMapper userTariffMapper;

    public UserTariffController(TariffAssignmentService tariffAssignmentService, UserTariffMapper userTariffMapper) {
        this.tariffAssignmentService = tariffAssignmentService;
        this.userTariffMapper = userTariffMapper;
    }

    @PostMapping("/{tariffId}")
    @Operation(
            summary = "Назначение специального тарифа.",
            description = "Позволяет назначить специальный тариф пользователю."
    )
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Void> assignTariff(
            @Parameter(description = "Идентификатор тарифа", required = true) @Min(1) @PathVariable("tariffId") Long tariffId,
            @Valid @RequestBody AssignTariffDto tariffDto
    ) {
        logger.info("Request: POST assign special tariff with id: {}, to user with id: {}.", tariffId, tariffDto.userId());

        this.tariffAssignmentService.assignTariffToUser(
                tariffDto.userId(),
                tariffId,
                tariffDto.customPricePerUnit(),
                tariffDto.discountPct()
        );

        logger.info("Special tariff with id: {} successfully assigned.", tariffId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/user/{userId}")
    @Operation(
            summary = "Отмена специального тарифа.",
            description = "Позволяет отменить (модератору) специальный тариф для пользователя по его id."
    )
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> canselTariffAsModerator(
            @Parameter(description = "Идентификатор пользователя", required = true) @Min(1) @PathVariable Long userId
    ) {
        logger.info("Request: DELETE /user/{} moderator cancel tariff for user.", userId);

        this.tariffAssignmentService.canselTariffFromUser(userId);

        logger.info("Special tariff with id: {} successfully cancelled.", userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/me")
    @Operation(
            summary = "Отмена специального тарифа.",
            description = "Позволяет отменить пользователю свой специальный тариф."
    )
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> canselTariffAsUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getId();
        logger.info("Request: DELETE /me user with id: {} cancel tariff.", userId);

        this.tariffAssignmentService.canselTariffFromUser(userId);

        logger.info("Special tariff with id: {} successfully cancelled.", userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Получение специального тарифа пользователя.",
            description = "Позволяет получить (модератору) активный специальный тариф пользователя по его id."
    )
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<UserTariffDto> getActiveSpecialTariffAsModerator(
            @Parameter(description = "Идентификатор пользователя", required = true) @Min(1) @PathVariable Long userId
    ) {
        logger.info("Request: GET /user/{} moderator fetch active special tariff.", userId);

        UserTariff userTariff = this.tariffAssignmentService.getActiveUserTariff(userId);

        logger.info("Special tariff with id: {} found for user with id: {}.", userTariff.getId(), userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.userTariffMapper.mapToDto(userTariff));
    }

    @GetMapping("/me")
    @Operation(
            summary = "Получение специального тарифа пользователя.",
            description = "Позволяет получить пользователю свой активный специальный тариф."
    )
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserTariffDto> getActiveSpecialTariffAsUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getId();
        logger.info("Request: GET /me user with id: {} fetch active special tariff.", userId);

        UserTariff userTariff = this.tariffAssignmentService.getActiveUserTariff(userId);

        logger.info("Special tariff with id: {} found for user with id: {}.", userTariff.getId(), userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.userTariffMapper.mapToDto(userTariff));
    }
}
