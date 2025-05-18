package ru.noleg.scootrent.controller.rolebased;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.noleg.scootrent.dto.user.UserDto;
import ru.noleg.scootrent.entity.user.Role;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.RepositoryException;
import ru.noleg.scootrent.mapper.UserMapper;
import ru.noleg.scootrent.service.user.UserService;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AdminController adminController;

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        // Arrange
        User user1 = mock(User.class);
        User user2 = mock(User.class);
        List<User> users = List.of(user1, user2);

        UserDto userDto1 = mock(UserDto.class);
        UserDto userDto2 = mock(UserDto.class);
        List<UserDto> expectedDtos = List.of(userDto1, userDto2);

        when(this.userService.getAllUsers()).thenReturn(users);
        when(this.userMapper.mapToDtos(users)).thenReturn(expectedDtos);

        // Act
        ResponseEntity<List<UserDto>> response = this.adminController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDtos, response.getBody());

        verify(this.userService, times(1)).getAllUsers();
        verify(this.userMapper, times(1)).mapToDtos(users);
    }

    @Test
    void getAllUsers_shouldReturnEmptyList() {
        // Arrange
        when(this.userService.getAllUsers()).thenReturn(List.of());
        when(this.userMapper.mapToDtos(anyList())).thenReturn(List.of());

        // Act
        ResponseEntity<List<UserDto>> response = this.adminController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    @Test
    void getAllUsers_shouldThrowException_whenServiceFails() {
        // Arrange
        doThrow(new RepositoryException("Database error")).when(this.userService).getAllUsers();

        // Act | Assert
        assertThrows(RepositoryException.class, () -> this.adminController.getAllUsers());
        verify(this.userService, times(1)).getAllUsers();
    }

    @Test
    void deleteUser_shouldCallServiceAndReturnOk() {
        // Arrange
        Long userId = 1L;
        doNothing().when(this.userService).delete(userId);

        // Act
        ResponseEntity<Void> response = this.adminController.deleteUser(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(this.userService, times(1)).delete(userId);
    }

    @Test
    void deleteUser_shouldThrowException_whenServiceFails() {
        // Arrange
        Long userId = 1L;
        doThrow(new NotFoundException("User not found.")).when(this.userService).delete(userId);

        // Act | Assert
        assertThrows(NotFoundException.class, () -> this.adminController.deleteUser(userId));
        verify(this.userService, times(1)).delete(userId);
    }

    @Test
    void updateUserRole_shouldUpdateRoleAndReturnOk() {
        // Arrange
        Long userId = 1L;
        Role newRole = Role.ROLE_ADMIN;
        doNothing().when(this.userService).updateUserRole(userId, newRole);

        // Act
        ResponseEntity<Void> response = this.adminController.updateUserRole(userId, newRole);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(this.userService, times(1)).updateUserRole(userId, newRole);
    }

    @Test
    void updateUserRole_shouldThrowException_whenServiceFails() {
        // Arrange
        Long userId = 1L;
        Role newRole = Role.ROLE_ADMIN;
        doThrow(new NotFoundException("User not found.")).when(this.userService).updateUserRole(userId, newRole);

        // Act | Assert
        assertThrows(NotFoundException.class, () -> this.adminController.updateUserRole(userId, newRole));
        verify(this.userService, times(1)).updateUserRole(userId, newRole);
    }
}