package ru.noleg.scootrent.controller.rolebased;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.noleg.scootrent.dto.rental.UserRentalHistoryDto;
import ru.noleg.scootrent.dto.user.UpdateUserDto;
import ru.noleg.scootrent.dto.user.UserDto;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.RepositoryException;
import ru.noleg.scootrent.mapper.RentalHistoryMapper;
import ru.noleg.scootrent.mapper.UserMapper;
import ru.noleg.scootrent.service.rental.RentalService;
import ru.noleg.scootrent.service.user.UserDetailsImpl;
import ru.noleg.scootrent.service.user.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RentalService rentalService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RentalHistoryMapper rentalHistoryMapper;

    @Mock
    private UserDetailsImpl userDetails;

    @InjectMocks
    private UserController userController;

    @Test
    void editUserProfile_shouldReturnUpdatedUserDto() {
        // Arrange
        Long userId = 1L;
        UpdateUserDto updateUserDto = mock(UpdateUserDto.class);
        User user = mock(User.class);
        User updatedUser = mock(User.class);
        UserDto expectedDto = mock(UserDto.class);

        when(this.userDetails.getId()).thenReturn(userId);
        when(this.userService.getUser(userId)).thenReturn(user);
        doNothing().when(this.userMapper).updateUserFromDto(updateUserDto, user);
        when(this.userService.save(user)).thenReturn(updatedUser);
        when(this.userMapper.mapToDto(updatedUser)).thenReturn(expectedDto);

        // Act
        ResponseEntity<UserDto> response = this.userController.editUserProfile(this.userDetails, updateUserDto);

        // Arrange
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDto, response.getBody());

        verify(this.userService, times(1)).getUser(userId);
        verify(this.userMapper, times(1)).updateUserFromDto(updateUserDto, user);
        verify(this.userService, times(1)).save(user);
        verify(this.userMapper, times(1)).mapToDto(updatedUser);
    }

    @Test
    void editUserProfile_shouldThrowException_whenUserNotFound() {
        // Arrange
        Long userId = 42L;
        UpdateUserDto dto = mock(UpdateUserDto.class);

        when(this.userDetails.getId()).thenReturn(userId);
        when(this.userService.getUser(userId)).thenThrow(new NotFoundException("User not found"));

        // Act | Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.userController.editUserProfile(userDetails, dto)
        );

        assertEquals("User not found", exception.getMessage());

        verify(this.userService, times(1)).getUser(userId);
        verify(this.userMapper, never()).updateUserFromDto(any(), any());
        verify(this.userService, never()).save(any());
    }

    @Test
    void getRentalHistory_shouldReturnUserRentalHistory() {
        // Arrange
        Long userId = 1L;
        List<Rental> rentals = List.of(mock(Rental.class), mock(Rental.class));
        List<UserRentalHistoryDto> dtos = List.of(mock(UserRentalHistoryDto.class), mock(UserRentalHistoryDto.class));

        when(this.userDetails.getId()).thenReturn(userId);
        when(this.rentalService.getRentalHistoryForUser(userId)).thenReturn(rentals);
        when(this.rentalHistoryMapper.mapToUserRentalDtos(rentals)).thenReturn(dtos);

        // Act
        ResponseEntity<List<UserRentalHistoryDto>> response = this.userController.getRentalHistory(this.userDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dtos, response.getBody());

        verify(this.rentalService, times(1)).getRentalHistoryForUser(userId);
        verify(this.rentalHistoryMapper, times(1)).mapToUserRentalDtos(rentals);
    }

    @Test
    void getRentalHistory_shouldThrowException_whenDbError() {
        // Arrange
        Long userId = 999L;

        when(this.userDetails.getId()).thenReturn(userId);
        when(this.rentalService.getRentalHistoryForUser(userId)).thenThrow(new RepositoryException("Database error"));

        // Act | Assert
        RepositoryException exception = assertThrows(RepositoryException.class,
                () -> this.userController.getRentalHistory(userDetails)
        );

        assertEquals("Database error", exception.getMessage());
        verify(this.rentalService, times(1)).getRentalHistoryForUser(userId);
        verify(this.rentalHistoryMapper, never()).mapToUserRentalDtos(any());
    }
}