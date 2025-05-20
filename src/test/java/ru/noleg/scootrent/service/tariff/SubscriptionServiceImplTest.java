package ru.noleg.scootrent.service.tariff;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.tariff.UserSubscription;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.UserNotFoundException;
import ru.noleg.scootrent.repository.TariffRepository;
import ru.noleg.scootrent.repository.UserRepository;
import ru.noleg.scootrent.repository.UserSubscriptionRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TariffRepository tariffRepository;

    @Mock
    private UserSubscriptionRepository userSubscriptionRepository;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    @Test
    void subscribeUser_shouldAssignSubscription_whenUserAndTariffAreValid() {
        // Arrange
        Long userId = 1L;
        Long tariffId = 2L;
        int limit = 100;

        User user = mock(User.class);
        Tariff tariff = mock(Tariff.class);

        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(this.userSubscriptionRepository.findActiveSubscriptionByUserAndTime(eq(userId), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        when(this.tariffRepository.findById(tariffId)).thenReturn(Optional.of(tariff));
        when(tariff.getIsActive()).thenReturn(true);
        when(tariff.getType()).thenReturn(TariffType.SUBSCRIPTION);
        when(tariff.getSubDurationDays()).thenReturn(30);

        // Act
        this.subscriptionService.subscribeUser(userId, tariffId, limit);

        // Assert
        verify(this.userRepository, times(1)).findById(userId);
        verify(this.userSubscriptionRepository, times(1))
                .findActiveSubscriptionByUserAndTime(eq(userId), any());
        verify(this.tariffRepository, times(1)).findById(tariffId);
        verify(this.userSubscriptionRepository, times(1)).save(any(UserSubscription.class));
    }

    @Test
    void subscribeUser_shouldThrowUserNotFoundException_whenUserNotFound() {
        // Arrange
        Long userId = 1L;
        Long tariffId = 2L;
        when(this.userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act | Assert
        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> this.subscriptionService.subscribeUser(userId, tariffId, 100));
        assertEquals("User with id 1 not found.", ex.getMessage());

        verify(this.userRepository, times(1)).findById(userId);
        verify(this.userSubscriptionRepository, never())
                .findActiveSubscriptionByUserAndTime(eq(userId), any());
        verify(this.tariffRepository, never()).findById(any());
        verify(this.userSubscriptionRepository, never()).save(any());
    }

    @Test
    void subscribeUser_shouldThrowBusinessLogicException_whenActiveSubscriptionExists() {
        // Arrange
        Long userId = 1L;
        Long tariffId = 2L;

        User user = mock(User.class);
        UserSubscription existingSub = mock(UserSubscription.class);
        when(existingSub.getId()).thenReturn(100L);

        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(this.userSubscriptionRepository.findActiveSubscriptionByUserAndTime(eq(userId), any()))
                .thenReturn(Optional.of(existingSub));

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.subscriptionService.subscribeUser(userId, tariffId, 100));
        assertEquals("User already been assigned a subscription with id: 100", ex.getMessage());

        verify(this.userRepository, times(1)).findById(userId);
        verify(this.userSubscriptionRepository, times(1))
                .findActiveSubscriptionByUserAndTime(eq(userId), any());
        verify(this.tariffRepository, never()).findById(any());
        verify(this.userSubscriptionRepository, never()).save(any());
    }

    @Test
    void subscribeUser_shouldThrowNotFoundException_whenTariffNotFound() {
        // Arrange
        Long userId = 1L;
        Long tariffId = 2L;

        User user = mock(User.class);
        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(this.userSubscriptionRepository.findActiveSubscriptionByUserAndTime(eq(userId), any()))
                .thenReturn(Optional.empty());

        when(this.tariffRepository.findById(tariffId)).thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.subscriptionService.subscribeUser(userId, tariffId, 100));
        assertEquals("Tariff with id 2 not found.", ex.getMessage());

        verify(this.userRepository, times(1)).findById(userId);
        verify(this.userSubscriptionRepository, times(1))
                .findActiveSubscriptionByUserAndTime(eq(userId), any());
        verify(this.tariffRepository, times(1)).findById(tariffId);
        verify(this.userSubscriptionRepository, never()).save(any());
    }

    @Test
    void subscribeUser_shouldThrow_whenTariffNotActive() {
        // Arrange
        Long userId = 1L;
        Long tariffId = 2L;

        User user = mock(User.class);
        Tariff tariff = mock(Tariff.class);

        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(this.userSubscriptionRepository.findActiveSubscriptionByUserAndTime(eq(userId), any()))
                .thenReturn(Optional.empty());
        when(this.tariffRepository.findById(tariffId)).thenReturn(Optional.of(tariff));
        when(tariff.getIsActive()).thenReturn(false);

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.subscriptionService.subscribeUser(userId, tariffId, 100));
        assertEquals("Tariff with id: 2 is not active.", ex.getMessage());

        verify(this.userRepository, times(1)).findById(userId);
        verify(this.userSubscriptionRepository, times(1))
                .findActiveSubscriptionByUserAndTime(eq(userId), any());
        verify(this.tariffRepository, times(1)).findById(tariffId);
        verify(this.userSubscriptionRepository, never()).save(any());
    }

    @Test
    void subscribeUser_shouldThrowBusinessLogicException_whenTariffIsNotSubscription() {
        // Arrange
        Long userId = 1L;
        Long tariffId = 2L;

        User user = mock(User.class);
        Tariff tariff = mock(Tariff.class);

        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(this.userSubscriptionRepository.findActiveSubscriptionByUserAndTime(eq(userId), any()))
                .thenReturn(Optional.empty());
        when(this.tariffRepository.findById(tariffId)).thenReturn(Optional.of(tariff));
        when(tariff.getIsActive()).thenReturn(true);
        when(tariff.getType()).thenReturn(TariffType.SPECIAL_TARIFF);

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.subscriptionService.subscribeUser(userId, tariffId, 100));
        assertEquals("Tariff with id: 2 is not a subscription.", ex.getMessage());

        verify(this.userRepository, times(1)).findById(userId);
        verify(this.userSubscriptionRepository, times(1))
                .findActiveSubscriptionByUserAndTime(eq(userId), any());
        verify(this.tariffRepository, times(1)).findById(tariffId);
        verify(this.userSubscriptionRepository, never()).save(any());
    }

    @Test
    void getActiveSubscription_shouldReturn_whenExists() {
        // Arrange
        Long userId = 1L;
        UserSubscription subscription = mock(UserSubscription.class);

        when(userSubscriptionRepository.findActiveSubscriptionByUserAndTime(eq(userId), any()))
                .thenReturn(Optional.of(subscription));

        // Act
        UserSubscription result = subscriptionService.getActiveSubscription(userId);

        // Assert
        assertEquals(subscription, result);
        verify(this.userSubscriptionRepository).findActiveSubscriptionByUserAndTime(eq(userId), any());
    }

    @Test
    void getActiveSubscription_shouldThrow_whenNotFound() {
        // Arrange
        Long userId = 1L;

        when(this.userSubscriptionRepository.findActiveSubscriptionByUserAndTime(eq(userId), any()))
                .thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.subscriptionService.getActiveSubscription(userId));
        assertEquals("Subscription not found for user with id: 1", ex.getMessage());

        verify(this.userSubscriptionRepository).findActiveSubscriptionByUserAndTime(eq(userId), any());
    }

    @Test
    void cancelSubscriptionFromUser_shouldDelete_whenExists() {
        // Arrange
        Long userId = 1L;
        UserSubscription sub = mock(UserSubscription.class);
        when(sub.getId()).thenReturn(2L);

        when(this.userSubscriptionRepository.findActiveSubscriptionByUserAndTime(eq(userId), any()))
                .thenReturn(Optional.of(sub));

        // Act
        this.subscriptionService.canselSubscriptionFromUser(userId);

        // Assert
        verify(this.userSubscriptionRepository, times(1)).delete(2L);
    }

    @Test
    void cancelSubscriptionFromUser_shouldThrowNotFoundException_whenNotFound() {
        // Arrange
        Long userId = 1L;
        when(this.userSubscriptionRepository.findActiveSubscriptionByUserAndTime(eq(userId), any()))
                .thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.subscriptionService.canselSubscriptionFromUser(userId));
        assertEquals("Subscription not found for user with id: 1", ex.getMessage());

        verify(this.userSubscriptionRepository, never()).delete(any());
    }
}