package ru.noleg.scootrent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import ru.noleg.scootrent.service.RentalService;
import ru.noleg.scootrent.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
@Tag(
        name = "Контроллер для пользователя.",
        description = "Позволяет изменять профиль/получать историю аренды."
)
public class UserController {

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

    @PatchMapping("/{id}") // будет браться из security context потом
    @Operation(
            summary = "Обновление профиля.",
            description = "Позволяет изменить профиль пользователя."
    )
    public ResponseEntity<UserDto> editUserProfile(
            @Parameter(description = "Идентификатор пользователя", required = true) @Min(1) @PathVariable("id") Long id,
            @Valid @RequestBody UpdateUserDto updateUserDto) {

        User user = this.userService.getUser(id);
        this.userMapper.updateUserFromDto(updateUserDto, user);
        User updateUser = this.userService.save(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.userMapper.mapToDto(updateUser));
    }

    @GetMapping("/{id}/rentalHistory") // будет браться из security context
    @Operation(
            summary = "История аренды пользователя.",
            description = "Позволяет получить историю аренды пользователя."
    )
    public ResponseEntity<List<UserRentalHistoryDto>> getRentalHistory(
            @Parameter(description = "Идентификатор пользователя", required = true) @Min(1) @PathVariable("id") Long id
    ) {
        List<Rental> rentals = this.rentalService.getRentalHistoryForUser(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.rentalHistoryMapper.mapToUserRentalDtos(rentals));
    }

    // TODO убрать в admin панель
    @GetMapping
    @Operation(
            summary = "Получение всех пользователей.",
            description = "Уберется в админ-панель."
    )
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDtos = this.userMapper.mapToDtos(this.userService.getAllUsers());
        return ResponseEntity.status(HttpStatus.OK).body(userDtos);
    }
}
