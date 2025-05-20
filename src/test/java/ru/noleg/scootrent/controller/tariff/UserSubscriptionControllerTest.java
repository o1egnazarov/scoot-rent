package ru.noleg.scootrent.controller.tariff;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.noleg.scootrent.dto.tariff.SubscribeUserDto;
import ru.noleg.scootrent.dto.tariff.UserSubscriptionDto;
import ru.noleg.scootrent.entity.tariff.UserSubscription;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.mapper.UserSubscriptionMapper;
import ru.noleg.scootrent.service.user.UserDetailsImpl;
import ru.noleg.scootrent.service.tariff.SubscriptionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSubscriptionControllerTest {

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private UserSubscriptionMapper userSubscriptionMapper;

    @InjectMocks
    private UserSubscriptionController userSubscriptionController;

    @Test
    void subscribeUserToTariff_shouldAssignSuccessfully() {
        // Arrange
        Long tariffId = 1L;
        SubscribeUserDto dto = new SubscribeUserDto(10L, 120);

        // Act
        ResponseEntity<Void> response = this.userSubscriptionController.subscribeUserToTariff(tariffId, dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(this.subscriptionService, times(1))
                .subscribeUser(dto.userId(), tariffId, dto.minutesUsageLimit());
    }

    @Test
    void subscribeUserToTariff_shouldThrownException_whenUserOrTariffNotFound() {
        // Arrange
        Long tariffId = 1L;
        SubscribeUserDto dto = new SubscribeUserDto(100L, 100);

        doThrow(new NotFoundException("User or tariff not found"))
                .when(this.subscriptionService).subscribeUser(dto.userId(), tariffId, dto.minutesUsageLimit());

        // Act | Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.userSubscriptionController.subscribeUserToTariff(tariffId, dto)
        );

        assertEquals("User or tariff not found", exception.getMessage());

        verify(this.subscriptionService, times(1))
                .subscribeUser(dto.userId(), tariffId, dto.minutesUsageLimit());
        verifyNoMoreInteractions(this.subscriptionService);
    }

    @Test
    void canselSubscriptionAsModerator_shouldCancelSuccessfully() {
        // Arrange
        Long userId = 1L;

        // Act
        ResponseEntity<Void> response = this.userSubscriptionController.canselSubscriptionAsModerator(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(this.subscriptionService, times(1)).canselSubscriptionFromUser(userId);
    }

    @Test
    void canselSubscriptionAsModerator_shouldThrownException_whenUserNotFound() {
        // Act
        Long userId = 100L;

        doThrow(new NotFoundException("User not found"))
                .when(this.subscriptionService).canselSubscriptionFromUser(userId);

        // Act | Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.userSubscriptionController.canselSubscriptionAsModerator(userId)
        );

        assertEquals("User not found", exception.getMessage());

        verify(this.subscriptionService, times(1)).canselSubscriptionFromUser(userId);
        verifyNoMoreInteractions(this.subscriptionService);
    }

    @Test
    void canselSubscriptionAsUser_shouldCancelSuccessfully() {
        // Arrange
        Long userId = 1L;
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);

        // Act
        ResponseEntity<Void> response = this.userSubscriptionController.canselSubscriptionAsUser(userDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(this.subscriptionService, times(1)).canselSubscriptionFromUser(userId);
    }

    @Test
    void canselSubscriptionAsUser_shouldThrownException_whenSubscriptionNotFound() {
        // Arrange
        Long userId = 42L;
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);

        doThrow(new NotFoundException("No subscription found"))
                .when(this.subscriptionService).canselSubscriptionFromUser(userId);

        // Act | Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.userSubscriptionController.canselSubscriptionAsUser(userDetails)
        );

        assertEquals("No subscription found", exception.getMessage());

        verify(this.subscriptionService, times(1)).canselSubscriptionFromUser(userId);
        verifyNoMoreInteractions(this.subscriptionService);
    }

    @Test
    void getActiveSubscriptionAsModerator_shouldReturnSubscription() {
        // Arrange
        Long userId = 1L;
        UserSubscription subscription = mock(UserSubscription.class);
        UserSubscriptionDto subscriptionDto = mock(UserSubscriptionDto.class);

        when(this.subscriptionService.getActiveSubscription(userId)).thenReturn(subscription);
        when(this.userSubscriptionMapper.mapToDto(subscription)).thenReturn(subscriptionDto);

        // Act
        ResponseEntity<UserSubscriptionDto> response = this.userSubscriptionController.getActiveSubscriptionAsModerator(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(subscriptionDto, response.getBody());

        verify(this.subscriptionService, times(1)).getActiveSubscription(userId);
        verify(this.userSubscriptionMapper, times(1)).mapToDto(subscription);
    }

    @Test
    void getActiveSubscriptionAsModerator_shouldThrownException_whenUserHasNoSubscription() {
        // Arrange
        Long userId = 100L;

        when(this.subscriptionService.getActiveSubscription(userId))
                .thenThrow(new NotFoundException("No active subscription"));

        // Act | Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.userSubscriptionController.getActiveSubscriptionAsModerator(userId)
        );

        assertEquals("No active subscription", exception.getMessage());

        verify(this.subscriptionService, times(1)).getActiveSubscription(userId);
        verifyNoMoreInteractions(this.subscriptionService);
    }

    @Test
    void getActiveSubscriptionAsUser_shouldReturnSubscription() {
        // Arrange
        Long userId = 42L;

        UserSubscription subscription = mock(UserSubscription.class);
        UserSubscriptionDto subscriptionDto = mock(UserSubscriptionDto.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);

        when(this.subscriptionService.getActiveSubscription(userId)).thenReturn(subscription);
        when(this.userSubscriptionMapper.mapToDto(subscription)).thenReturn(subscriptionDto);

        // Act
        ResponseEntity<UserSubscriptionDto> response = this.userSubscriptionController.getActiveSubscriptionAsUser(userDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(subscriptionDto, response.getBody());

        verify(this.subscriptionService, times(1)).getActiveSubscription(userId);
        verify(this.userSubscriptionMapper, times(1)).mapToDto(subscription);
    }

    @Test
    void getActiveSubscriptionAsUser_shouldThrownException_whenUserHasNoSubscription() {
        // Arrange
        Long userId = 42L;
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);

        // Act | Assert
        when(subscriptionService.getActiveSubscription(userId))
                .thenThrow(new NotFoundException("No active subscription"));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.userSubscriptionController.getActiveSubscriptionAsUser(userDetails)
        );

        assertEquals("No active subscription", exception.getMessage());

        verify(subscriptionService, times(1)).getActiveSubscription(userId);
        verifyNoMoreInteractions(this.subscriptionService);
    }
}