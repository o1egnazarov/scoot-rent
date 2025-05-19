package ru.noleg.scootrent.service.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        // Arrange
        String username = "username";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");

        when(this.userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());

        verify(this.userRepository, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException_whenUserNotFound() {
        // Arrange
        String username = "notfound";
        when(this.userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act | Assert
        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class,
                () -> this.userDetailsService.loadUserByUsername(username)
        );
        assertEquals("User with username notfound not found.", ex.getMessage());

        verify(this.userRepository, times(1)).findByUsername(username);
    }

}