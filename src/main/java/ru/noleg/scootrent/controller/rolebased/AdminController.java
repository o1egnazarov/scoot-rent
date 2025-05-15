package ru.noleg.scootrent.controller.rolebased;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.user.UserDto;
import ru.noleg.scootrent.entity.user.Role;
import ru.noleg.scootrent.mapper.UserMapper;
import ru.noleg.scootrent.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Validated
@Tag(
        name = "Контроллер администратора.",
        description = "Управляет пользователями системы."
)
@SecurityRequirement(name = "JWT")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final UserService userService;
    private final UserMapper userMapper;

    public AdminController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/users")
    @Operation(
            summary = "Получение всех пользователей.",
            description = "Позволяет получить всех пользователей системы."
    )
    public ResponseEntity<List<UserDto>> getAllUsers() {
        logger.info("Request: GET fetching all users.");

        List<UserDto> userDtos = this.userMapper.mapToDtos(this.userService.getAllUsers());

        logger.debug("Got {} users.", userDtos.size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDtos);
    }

    @DeleteMapping("/users/{userId}")
    @Operation(
            summary = "Удаление пользователя.",
            description = "Позволяет удалить любого пользователя по id, кроме тех у кого роль: ADMIN."
    )
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @Min(1) @PathVariable("userId") Long userId
    ) {
        logger.info("Request: DELETE delete user with id: {}.", userId);

        this.userService.delete(userId);

        logger.info("User with id: {}, successfully deleted.", userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


    @PatchMapping("/users/{userId}")
    @Operation(
            summary = "Обновление роли.",
            description = "Позволяет обновить роль пользователя."
    )
    public ResponseEntity<Void> updateUserRole(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @Min(1) @PathVariable("userId") Long userId,

            @Parameter(description = "Роль пользователя", required = true)
            @RequestParam("role") @NotNull Role role
    ) {
        logger.info("Request: PATCH update user with id: {} on role: {}.", userId, role);

        this.userService.updateUserRole(userId, role);

        logger.info("Role for user with id: {} successfully updated.", userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}