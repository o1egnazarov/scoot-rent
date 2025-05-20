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
import ru.noleg.scootrent.dto.tariff.SubscribeUserDto;
import ru.noleg.scootrent.dto.tariff.UserSubscriptionDto;
import ru.noleg.scootrent.entity.tariff.UserSubscription;
import ru.noleg.scootrent.mapper.UserSubscriptionMapper;
import ru.noleg.scootrent.service.user.UserDetailsImpl;
import ru.noleg.scootrent.service.tariff.SubscriptionService;

@RestController
@RequestMapping("/api/user-subscriptions")
@Validated
@Tag(
        name = "Контроллер для назначения подписки.",
        description = "Позволяет назначать и управлять подписками пользователей."
)
@SecurityRequirement(name = "JWT")
public class UserSubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(UserSubscriptionController.class);

    private final SubscriptionService subscriptionService;
    private final UserSubscriptionMapper userSubscriptionMapper;

    public UserSubscriptionController(SubscriptionService subscriptionService, UserSubscriptionMapper userSubscriptionMapper) {
        this.subscriptionService = subscriptionService;
        this.userSubscriptionMapper = userSubscriptionMapper;
    }

    @PostMapping("/{tariffId}")
    @Operation(
            summary = "Назначение подписки.",
            description = "Позволяет назначить подписку пользователю."
    )
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Void> subscribeUserToTariff(
            @Parameter(description = "Идентификатор тарифа", required = true) @Min(1) @PathVariable("tariffId") Long tariffId,
            @Valid @RequestBody SubscribeUserDto subscribeDto
    ) {
        logger.info("Request: POST assign subscription with id: {}, to user with id: {}.", tariffId, subscribeDto.userId());

        this.subscriptionService.subscribeUser(subscribeDto.userId(), tariffId, subscribeDto.minutesUsageLimit());

        logger.info("Subscription with id: {} successfully assigned.", tariffId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/user/{userId}")
    @Operation(
            summary = "Отмена подписки.",
            description = "Позволяет отменить (модератору) подписку для пользователя по его id."
    )
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<Void> canselSubscriptionAsModerator(
            @Parameter(description = "Идентификатор пользователя", required = true) @Min(1) @PathVariable Long userId
    ) {
        logger.info("Request: DELETE /user/{} moderator cancel subscription for user.", userId);

        this.subscriptionService.canselSubscriptionFromUser(userId);

        logger.info("Subscription with id: {} successfully cancelled.", userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/me")
    @Operation(
            summary = "Отмена подписки.",
            description = "Позволяет отменить пользователю свою подписку."
    )
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> canselSubscriptionAsUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getId();
        logger.info("Request: DELETE /me user with id: {} cancel subscription.", userId);

        this.subscriptionService.canselSubscriptionFromUser(userId);

        logger.info("Subscription with id: {} successfully cancelled.", userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Получение подписки пользователя.",
            description = "Позволяет получить модератору активную подписку пользователя по его id."
    )
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<UserSubscriptionDto> getActiveSubscriptionAsModerator(
            @Parameter(description = "Идентификатор пользователя", required = true) @Min(1) @PathVariable Long userId
    ) {
        logger.info("Request: GET /user/{} moderator fetch active user subscription.", userId);

        UserSubscription userSubscription = this.subscriptionService.getActiveSubscription(userId);

        logger.info("Subscription with id: {} found for user with id: {}.", userSubscription.getId(), userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.userSubscriptionMapper.mapToDto(userSubscription));
    }

    @GetMapping("/me")
    @Operation(
            summary = "Получение подписки пользователя.",
            description = "Позволяет получить пользователю свою активную подписку."
    )
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserSubscriptionDto> getActiveSubscriptionAsUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getId();
        logger.info("Request: GET /me user with id: {} fetch active subscription.", userId);

        UserSubscription userSubscription = this.subscriptionService.getActiveSubscription(userId);

        logger.info("Subscription with id: {} found for user with id: {}.", userSubscription.getId(), userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.userSubscriptionMapper.mapToDto(userSubscription));
    }
}
