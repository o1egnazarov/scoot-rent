package ru.noleg.scootrent.service.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.user.Role;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.UserNotFoundException;
import ru.noleg.scootrent.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceDefaultImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceDefaultImpl userService;

    @Test
    void save_shouldSaveAndReturnUser_whenUserIsValid() {
        // Arrange
        User user = mock(User.class);
        User savedUser = mock(User.class);

        when(this.userRepository.save(user)).thenReturn(savedUser);

        // Act
        User result = this.userService.save(user);

        // Assert
        assertEquals(savedUser, result);
        verify(this.userRepository, times(1)).save(user);
    }

    @Test
    void delete_shouldDeleteUser_whenUserExistsAndIsNotAdmin() {
        // Arrange
        Long userId = 1L;
        User user = mock(User.class);
        when(user.getRole()).thenReturn(Role.ROLE_USER);

        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        this.userService.delete(userId);

        // Assert
        verify(this.userRepository, times(1)).findById(userId);
        verify(this.userRepository, times(1)).delete(userId);
    }

    @Test
    void delete_shouldThrowNotFoundException_whenUserNotFound() {
        // Arrange
        Long userId = 1L;
        when(this.userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.userService.delete(userId));
        assertEquals("User with ID: 1 not found.", ex.getMessage());

        verify(this.userRepository, times(1)).findById(userId);
        verify(this.userRepository, never()).delete(anyLong());
    }

    @Test
    void delete_shouldThrowBusinessLogicException_whenUserIsAdmin() {
        // Arrange
        Long userId = 1L;
        User adminUser = new User();
        adminUser.setRole(Role.ROLE_ADMIN);

        when(this.userRepository.findById(userId)).thenReturn(Optional.of(adminUser));

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.userService.delete(userId));
        assertEquals("You can't delete an administrator.", ex.getMessage());

        verify(this.userRepository, times(1)).findById(userId);
        verify(this.userRepository, never()).delete(anyLong());
    }

    @Test
    void getUser_shouldReturnUser_whenUserExists() {
        // Arrange
        Long userId = 1L;
        User user = mock(User.class);

        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User result = this.userService.getUser(userId);

        // Assert
        assertEquals(user, result);

        verify(this.userRepository, times(1)).findById(userId);
    }

    @Test
    void getUser_shouldThrowUserNotFoundException_whenUserNotFound() {
        // Arrange
        Long userId = 1L;
        when(this.userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act | Assert
        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> this.userService.getUser(userId));
        assertEquals("User with id: 1 not found.", ex.getMessage());

        verify(this.userRepository, times(1)).findById(userId);
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        // Arrange
        List<User> users = List.of(mock(User.class), mock(User.class));
        when(this.userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = this.userService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    void updateUserRole_shouldUpdateRole() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setRole(Role.ROLE_USER);

        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        this.userService.updateUserRole(userId, Role.ROLE_MODERATOR);

        // Assert
        assertEquals(Role.ROLE_MODERATOR, user.getRole());
        verify(this.userRepository, times(1)).findById(userId);
        verify(this.userRepository, times(1)).save(user);
    }
}