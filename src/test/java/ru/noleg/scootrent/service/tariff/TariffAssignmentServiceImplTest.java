package ru.noleg.scootrent.service.tariff;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.UserTariff;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.UserNotFoundException;
import ru.noleg.scootrent.repository.TariffRepository;
import ru.noleg.scootrent.repository.UserRepository;
import ru.noleg.scootrent.repository.UserTariffRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TariffAssignmentServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TariffRepository tariffRepository;
    @Mock
    private UserTariffRepository userTariffRepository;

    @InjectMocks
    private TariffAssignmentServiceImpl tariffAssignmentService;

    @Test
    void assignTariffToUser_shouldAssignUserTariff_whenUserAndTariffAreValid() {
        // Arrange
        Long userId = 1L;
        Long tariffId = 2L;
        BigDecimal customPrice = BigDecimal.valueOf(5.25);
        Integer discountPct = 20;

        User user = mock(User.class);
        Tariff tariff = mock(Tariff.class);

        when(tariff.getIsActive()).thenReturn(true);
        when(tariff.getType()).thenReturn(TariffType.SPECIAL_TARIFF);

        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(this.userTariffRepository.findActiveTariffByUserAndTime(eq(userId), any())).thenReturn(Optional.empty());
        when(this.tariffRepository.findById(tariffId)).thenReturn(Optional.of(tariff));

        // Act
        this.tariffAssignmentService.assignTariffToUser(userId, tariffId, customPrice, discountPct);

        // Assert
        verify(this.userRepository, times(1)).findById(userId);
        verify(this.userTariffRepository, times(1))
                .findActiveTariffByUserAndTime(eq(userId), any());
        verify(this.tariffRepository, times(1)).findById(tariffId);
        verify(this.userTariffRepository, times(1)).save(any(UserTariff.class));
    }

    @Test
    void assignTariffToUser_shouldThrowUserNotFoundException_whenUserNotFound() {
        // Arrange
        Long userId = 1L;
        Long tariffId = 2L;
        BigDecimal customPrice = BigDecimal.valueOf(5.25);
        Integer discountPct = 20;

        when(this.userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act | Assert
        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> this.tariffAssignmentService.assignTariffToUser(userId, tariffId, customPrice, discountPct));
        assertEquals("User with id 1 not found.", ex.getMessage());

        verify(this.userRepository, times(1)).findById(userId);
        verify(this.userTariffRepository, never())
                .findActiveTariffByUserAndTime(eq(userId), any());
        verify(this.tariffRepository, never()).findById(any());
        verify(this.userTariffRepository, never()).save(any());
    }

    @Test
    void assignTariffToUser_shouldThrowBusinessLogicException_whenActiveUserTariffExists() {
        // Arrange
        Long userId = 1L;
        Long tariffId = 2L;
        BigDecimal customPrice = BigDecimal.valueOf(5.25);
        Integer discountPct = 20;

        User user = mock(User.class);
        UserTariff existingTariff = mock(UserTariff.class);
        when(existingTariff.getId()).thenReturn(100L);

        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(this.userTariffRepository.findActiveTariffByUserAndTime(eq(userId), any()))
                .thenReturn(Optional.of(existingTariff));

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.tariffAssignmentService.assignTariffToUser(userId, tariffId, customPrice, discountPct));
        assertEquals("User already been assigned a special tariff with id: 100", ex.getMessage());

        verify(this.userRepository, times(1)).findById(userId);
        verify(this.userTariffRepository, times(1))
                .findActiveTariffByUserAndTime(eq(userId), any());
        verify(this.tariffRepository, never()).findById(any());
        verify(this.userTariffRepository, never()).save(any());
    }

    @Test
    void assignTariffToUser_shouldThrowNotFoundException_whenTariffNotFound() {
        // Arrange
        Long userId = 1L;
        Long tariffId = 2L;
        BigDecimal customPrice = BigDecimal.valueOf(5.25);
        Integer discountPct = 20;

        User user = mock(User.class);
        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(this.userTariffRepository.findActiveTariffByUserAndTime(eq(userId), any()))
                .thenReturn(Optional.empty());

        when(this.tariffRepository.findById(tariffId)).thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.tariffAssignmentService.assignTariffToUser(userId, tariffId, customPrice, discountPct));
        assertEquals("Tariff with id 2 not found.", ex.getMessage());

        verify(this.userRepository, times(1)).findById(userId);
        verify(this.userTariffRepository, times(1))
                .findActiveTariffByUserAndTime(eq(userId), any());
        verify(this.tariffRepository, times(1)).findById(tariffId);
        verify(this.userTariffRepository, never()).save(any());
    }

    @Test
    void assignTariffToUser_shouldThrow_whenTariffNotActive() {
        // Arrange
        Long userId = 1L;
        Long tariffId = 2L;
        BigDecimal customPrice = BigDecimal.valueOf(5.25);
        Integer discountPct = 20;

        User user = mock(User.class);
        Tariff tariff = mock(Tariff.class);

        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(this.userTariffRepository.findActiveTariffByUserAndTime(eq(userId), any()))
                .thenReturn(Optional.empty());
        when(this.tariffRepository.findById(tariffId)).thenReturn(Optional.of(tariff));
        when(tariff.getIsActive()).thenReturn(false);

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.tariffAssignmentService.assignTariffToUser(userId, tariffId, customPrice, discountPct));
        assertEquals("Tariff with id: 2 is not active.", ex.getMessage());

        verify(this.userRepository, times(1)).findById(userId);
        verify(this.userTariffRepository, times(1))
                .findActiveTariffByUserAndTime(eq(userId), any());
        verify(this.tariffRepository, times(1)).findById(tariffId);
        verify(this.userTariffRepository, never()).save(any());
    }

    @Test
    void assignTariffToUser_shouldThrowBusinessLogicException_whenTariffIsNotSpecialTariff() {
        // Arrange
        Long userId = 1L;
        Long tariffId = 2L;
        BigDecimal customPrice = BigDecimal.valueOf(5.25);
        Integer discountPct = 20;

        User user = mock(User.class);
        Tariff tariff = mock(Tariff.class);

        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(this.userTariffRepository.findActiveTariffByUserAndTime(eq(userId), any()))
                .thenReturn(Optional.empty());
        when(this.tariffRepository.findById(tariffId)).thenReturn(Optional.of(tariff));
        when(tariff.getIsActive()).thenReturn(true);
        when(tariff.getType()).thenReturn(TariffType.SUBSCRIPTION);

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.tariffAssignmentService.assignTariffToUser(userId, tariffId, customPrice, discountPct));
        assertEquals("Tariff with id: 2 is a subscription.", ex.getMessage());

        verify(this.userRepository, times(1)).findById(userId);
        verify(this.userTariffRepository, times(1))
                .findActiveTariffByUserAndTime(eq(userId), any());
        verify(this.tariffRepository, times(1)).findById(tariffId);
        verify(this.userTariffRepository, never()).save(any());
    }

    @Test
    void getActiveUserTariff_shouldReturn_whenExists() {
        // Arrange
        Long userId = 1L;
        UserTariff userTariff = mock(UserTariff.class);

        when(this.userTariffRepository.findActiveTariffByUserAndTime(eq(userId), any()))
                .thenReturn(Optional.of(userTariff));

        // Act
        UserTariff result = this.tariffAssignmentService.getActiveUserTariff(userId);

        // Assert
        assertEquals(userTariff, result);
        verify(this.userTariffRepository).findActiveTariffByUserAndTime(eq(userId), any());
    }

    @Test
    void getActiveUserTariff_shouldThrow_whenNotFound() {
        // Arrange
        Long userId = 1L;
        when(this.userTariffRepository.findActiveTariffByUserAndTime(eq(userId), any()))
                .thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.tariffAssignmentService.getActiveUserTariff(userId)
        );
        assertEquals("Special tariff not found for user with id: 1", ex.getMessage());

        verify(this.userTariffRepository).findActiveTariffByUserAndTime(eq(userId), any());
    }

    @Test
    void canselTariffFromUser_shouldDelete_whenExists() {
        // Arrange
        Long userId = 1L;
        UserTariff userTariff = mock(UserTariff.class);
        when(userTariff.getId()).thenReturn(2L);

        when(this.userTariffRepository.findActiveTariffByUserAndTime(eq(userId), any()))
                .thenReturn(Optional.of(userTariff));

        // Act
        this.tariffAssignmentService.canselTariffFromUser(userId);

        // Assert
        verify(this.userTariffRepository, times(1)).delete(2L);
    }

    @Test
    void canselTariffFromUser_shouldThrowNotFoundException_whenNotFound() {
        // Arrange
        Long userId = 1L;
        when(this.userTariffRepository.findActiveTariffByUserAndTime(eq(userId), any()))
                .thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.tariffAssignmentService.canselTariffFromUser(userId)
        );
        assertEquals("Special tariff not found for user with id: 1", ex.getMessage());

        verify(this.userTariffRepository, never()).delete(any());
    }
}