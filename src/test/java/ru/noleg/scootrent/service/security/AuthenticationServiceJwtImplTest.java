package ru.noleg.scootrent.service.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.repository.UserRepository;
import ru.noleg.scootrent.service.auth.AuthenticationServiceJwtImpl;
import ru.noleg.scootrent.service.security.jwt.JwtUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceJwtImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceJwtImpl authService;

    @Test
    void signUp_shouldSaveUser_whenValidData() {
        // Arrange
        Long userId = 1L;
        String username = "user";
        String password = "password";
        String email = "email@gmail.com";
        String phoneNumber = "123456789";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phoneNumber);

        User savedUser = new User();
        savedUser.setId(userId);

        when(this.userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(this.userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(this.userRepository.findByPhone(phoneNumber)).thenReturn(Optional.empty());
        when(this.passwordEncoder.encode(password)).thenReturn("encodedPass");
        when(this.userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        Long result = this.authService.signUp(user);

        // Assert
        assertEquals(userId, result);
        assertEquals("encodedPass", user.getPassword());

        verify(this.userRepository, times(1)).findByUsername(username);
        verify(this.userRepository, times(1)).findByEmail(email);
        verify(this.userRepository, times(1)).findByPhone(phoneNumber);
        verify(this.passwordEncoder, times(1)).encode(password);
        verify(this.userRepository, times(1)).save(user);
    }

    @Test
    void signUp_shouldThrowBusinessLogicException_whenUsernameExists() {
        // Arrange
        String username = "user";
        User user = mock(User.class);
        when(user.getUsername()).thenReturn(username);

        when(this.userRepository.findByUsername(username)).thenReturn(Optional.of(mock(User.class)));

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () -> this.authService.signUp(user));
        assertEquals("User with username: user already exists.", ex.getMessage());

        verify(this.userRepository, times(1)).findByUsername(username);
        verifyNoMoreInteractions(this.userRepository);
        verify(this.passwordEncoder, never()).encode(any());
    }

    @Test
    void signUp_shouldThrowException_whenEmailExists() {
        // Arrange
        String username = "user";
        String email = "email@gmail.com";

        User user = mock(User.class);
        when(user.getUsername()).thenReturn(username);
        when(user.getEmail()).thenReturn(email);

        when(this.userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(this.userRepository.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.authService.signUp(user)
        );
        assertEquals("User with email: email@gmail.com already exists.", ex.getMessage());

        verify(this.userRepository, times(1)).findByUsername(username);
        verify(this.userRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(this.userRepository);
        verify(this.passwordEncoder, never()).encode(any());
    }

    @Test
    void signUp_shouldThrowException_whenPhoneExists() {
        // Arrange
        String username = "user";
        String email = "email@gmail.com";
        String phoneNumber = "123456789";

        User user = mock(User.class);
        when(user.getUsername()).thenReturn(username);
        when(user.getEmail()).thenReturn(email);
        when(user.getPhone()).thenReturn(phoneNumber);

        when(this.userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(this.userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(this.userRepository.findByPhone(phoneNumber)).thenReturn(Optional.of(mock(User.class)));

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () -> this.authService.signUp(user));
        assertEquals("User with phone: 123456789 already exists.", ex.getMessage());

        verify(this.userRepository, times(1)).findByUsername(username);
        verify(this.userRepository, times(1)).findByEmail(email);
        verify(this.userRepository, times(1)).findByPhone(phoneNumber);
        verifyNoMoreInteractions(this.userRepository);
        verify(this.passwordEncoder, never()).encode(any());
    }

    @Test
    void signIn_shouldAuthenticateAndReturnToken_whenCredentialsValid() {
        // Arrange
        String username = "user";
        String password = "pass";
        String token = "jwt-token";
        UserDetails userDetails = mock(UserDetails.class);

        when(this.userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(this.jwtUtil.generateToken(userDetails)).thenReturn(token);

        // Act
        String result = authService.signIn(username, password);

        // Assert
        assertEquals(token, result);

        verify(this.authenticationManager).authenticate(any());
        verify(this.jwtUtil).generateToken(userDetails);
    }

    @Test
    void signIn_shouldThrowBadCredentialsException_whenAuthenticationFails() {
        // Arrange
        String username = "user";
        String password = "wrong";

        // Act | Assert
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any());

        assertThrows(BadCredentialsException.class, () -> this.authService.signIn(username, password));

        verify(this.authenticationManager).authenticate(any());
        verify(this.userDetailsService, never()).loadUserByUsername(username);
    }
}