package ru.noleg.scootrent.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.noleg.scootrent.dto.rental.ShortRentalDto;
import ru.noleg.scootrent.dto.user.UpdateUserDto;
import ru.noleg.scootrent.dto.user.UserDto;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.mapper.RentalMapper;
import ru.noleg.scootrent.mapper.UserMapper;
import ru.noleg.scootrent.service.RentalService;
import ru.noleg.scootrent.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final RentalService rentalService;
    private final RentalMapper rentalMapper;
    private final UserMapper userMapper;

    public UserController(UserService userService,
                          RentalService rentalService,
                          RentalMapper rentalMapper,
                          UserMapper userMapper) {
        this.userService = userService;
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
        this.userMapper = userMapper;
    }

    @PatchMapping("/{id}") // будет браться из security context потом
    public ResponseEntity<UserDto> editUserProfile(@PathVariable("id") Long id,
                                                   @Valid @RequestBody UpdateUserDto updateUserDto) {

        User user = this.userService.getUser(id);
        this.userMapper.updateUserFromDto(updateUserDto, user);
        User updateUser = this.userService.save(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.userMapper.mapToDto(updateUser));
    }

    @GetMapping("/{id}/rentalHistory") // будет браться из security context
    public ResponseEntity<List<ShortRentalDto>> getRentalHistory(@PathVariable("id") Long id) {
        List<Rental> rentals = this.rentalService.getRentalHistoryForUser(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.rentalMapper.mapToShortDtos(rentals));
    }

    // TODO убрать в admin панель
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDtos = this.userMapper.mapToDtos(this.userService.getAllUsers());
        return ResponseEntity.status(HttpStatus.OK).body(userDtos);
    }
}
