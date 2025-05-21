package ru.noleg.scootrent.controller.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.noleg.scootrent.dto.security.JwtResponse;
import ru.noleg.scootrent.dto.security.SignIn;
import ru.noleg.scootrent.dto.security.SignUp;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.mapper.UserMapper;
import ru.noleg.scootrent.service.auth.AuthenticationService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void signUp_shouldRegisterUserAndReturnId() {
        // Arrange
        Long userId = 42L;
        SignUp request = mock(SignUp.class);
        User mappedUser = mock(User.class);

        when(this.userMapper.mapToRegisterEntityFromSignUp(request)).thenReturn(mappedUser);
        when(this.authenticationService.signUp(mappedUser)).thenReturn(userId);

        // Act
        ResponseEntity<Long> response = this.authenticationController.signUp(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userId, response.getBody());

        verify(this.userMapper, times(1)).mapToRegisterEntityFromSignUp(request);
        verify(this.authenticationService, times(1)).signUp(mappedUser);
    }

    @Test
    void signUp_shouldThrownException_whenRegistrationFails() {
        // Arrange
        SignUp request = mock(SignUp.class);
        User mappedUser = new User();

        when(this.userMapper.mapToRegisterEntityFromSignUp(request)).thenReturn(mappedUser);
        when(this.authenticationService.signUp(mappedUser)).thenThrow(new RuntimeException("Registration error"));

        // Act | Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> this.authenticationController.signUp(request)
        );

        assertEquals("Registration error", exception.getMessage());

        verify(this.userMapper, times(1)).mapToRegisterEntityFromSignUp(request);
        verify(this.authenticationService, times(1)).signUp(mappedUser);
    }

    @Test
    void signIn_shouldAuthenticateUserAndReturnJwt() {
        // Arrange
        SignIn request = new SignIn("existingUser", "securePassword");
        String expectedToken = UUID.randomUUID().toString();

        when(this.authenticationService.signIn(request.username(), request.password())).thenReturn(expectedToken);

        // Act
        ResponseEntity<JwtResponse> response = this.authenticationController.signIn(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedToken, response.getBody().token());

        verify(this.authenticationService, times(1)).signIn(request.username(), request.password());
    }

    @Test
    void signIn_shouldThrownException_whenAuthenticationFails() {
        // Arrange
        SignIn request = new SignIn("badUser", "wrongPassword");

        when(this.authenticationService.signIn(anyString(), anyString()))
                .thenThrow(new RuntimeException("Invalid credentials"));


        // Act | Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> this.authenticationController.signIn(request)
        );

        assertEquals("Invalid credentials", exception.getMessage());
        verify(this.authenticationService, times(1))
                .signIn(request.username(), request.password());
    }
}