package ru.noleg.scootrent.controller.rolebased;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.rental.UserRentalHistoryDto;
import ru.noleg.scootrent.dto.user.UpdateUserDto;
import ru.noleg.scootrent.dto.user.UserDto;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.mapper.RentalHistoryMapper;
import ru.noleg.scootrent.mapper.UserMapper;
import ru.noleg.scootrent.service.rental.RentalService;
import ru.noleg.scootrent.service.user.UserDetailsImpl;
import ru.noleg.scootrent.service.user.UserService;

import java.util.List;

@PreAuthorize("(hasRole('USER'))")
@RestController
@RequestMapping("/api/users")
@Validated
@Tag(
        name = "Контроллер для пользователя.",
        description = "Позволяет изменять профиль/получать историю аренды."
)
@SecurityRequirement(name = "JWT")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final RentalService rentalService;
    private final UserMapper userMapper;
    private final RentalHistoryMapper rentalHistoryMapper;

    public UserController(UserService userService,
                          RentalService rentalService,
                          UserMapper userMapper,
                          RentalHistoryMapper rentalHistoryMapper) {
        this.userService = userService;
        this.rentalService = rentalService;
        this.rentalHistoryMapper = rentalHistoryMapper;
        this.userMapper = userMapper;
    }

    @PatchMapping("/me")
    @Operation(
            summary = "Обновление профиля.",
            description = "Позволяет изменить профиль пользователя."
    )
    public ResponseEntity<UserDto> editUserProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UpdateUserDto updateUserDto
    ) {
        Long id = userDetails.getId();
        logger.info("Request: PUT /me update profile for user with id: {}.", id);

        User user = this.userService.getUser(id);
        this.userMapper.updateUserFromDto(updateUserDto, user);
        User updateUser = this.userService.save(user);

        logger.info("Profile for user with id: {} successfully updated.", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.userMapper.mapToDto(updateUser));
    }

    @GetMapping("/me/history")
    @Operation(
            summary = "История аренды пользователя.",
            description = "Позволяет получить историю аренды пользователя."
    )
    public ResponseEntity<List<UserRentalHistoryDto>> getRentalHistory(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long id = userDetails.getId();
        logger.info("Request: GET /me/history fetching rental history by user with id: {}.", id);
        List<Rental> rentalHistory = this.rentalService.getRentalHistoryForUser(id);

        logger.info("Got {} records rental history for user with id {}.", rentalHistory.size(), id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.rentalHistoryMapper.mapToUserRentalDtos(rentalHistory));
    }
}
