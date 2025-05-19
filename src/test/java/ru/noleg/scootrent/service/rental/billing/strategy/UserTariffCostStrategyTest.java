package ru.noleg.scootrent.service.rental.billing.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.UserTariff;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.UserTariffRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTariffCostStrategyTest {

    @Mock
    private UserTariffRepository userTariffRepository;

    @InjectMocks
    private UserTariffCostStrategy userTariffCostStrategy;


    @Test
    void getSupportedTariffType_shouldReturnSpecialTariff() {
        assertEquals(TariffType.SPECIAL_TARIFF, this.userTariffCostStrategy.getSupportedTariffType());
    }

    @Test
    void calculate_shouldReturnCorrectCost_withCustomPriceAndDiscount() {
        // Arrange
        Long userId = 1L;
        int discount = 10;
        int unlockFee = 5;
        BigDecimal customPrice = BigDecimal.valueOf(2.5);
        Duration rentalDuration = Duration.ofMinutes(30);

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Tariff tariff = mock(Tariff.class);
        when(tariff.getTitle()).thenReturn("Gold");
        when(tariff.getUnlockFee()).thenReturn(unlockFee);

        UserTariff userTariff = mock(UserTariff.class);
        when(userTariff.getTariff()).thenReturn(tariff);
        when(userTariff.getCustomPricePerMinute()).thenReturn(customPrice);
        when(userTariff.getDiscountPct()).thenReturn(discount);

        when(this.userTariffRepository.findActiveTariffByUserAndTime(eq(userId), any(LocalDateTime.class)))
                .thenReturn(Optional.of(userTariff));

        // Act
        BigDecimal cost = this.userTariffCostStrategy.calculate(user, tariff, rentalDuration);

        // Assert
        assertEquals(0, cost.compareTo(BigDecimal.valueOf(72.5)));

        verify(this.userTariffRepository, times(1))
                .findActiveTariffByUserAndTime(eq(userId), any(LocalDateTime.class));
    }

    @Test
    void calculate_shouldReturnCorrectCost_withoutDiscount() {
        // Arrange
        Long userId = 1L;
        int unlockFee = 5;
        BigDecimal customPrice = BigDecimal.valueOf(3.0);
        Duration rentalDuration = Duration.ofMinutes(20);

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Tariff tariff = mock(Tariff.class);
        when(tariff.getTitle()).thenReturn("Silver");
        when(tariff.getUnlockFee()).thenReturn(unlockFee);

        UserTariff userTariff = mock(UserTariff.class);
        when(userTariff.getTariff()).thenReturn(tariff);
        when(userTariff.getCustomPricePerMinute()).thenReturn(customPrice);
        when(userTariff.getDiscountPct()).thenReturn(null);

        when(this.userTariffRepository.findActiveTariffByUserAndTime(eq(userId), any(LocalDateTime.class)))
                .thenReturn(Optional.of(userTariff));

        // Act
        BigDecimal result = this.userTariffCostStrategy.calculate(user, tariff, rentalDuration);

        // Assert
        assertEquals(0, result.compareTo(BigDecimal.valueOf(65)));

        verify(this.userTariffRepository, times(1))
                .findActiveTariffByUserAndTime(eq(userId), any(LocalDateTime.class));
    }

    @Test
    void calculate_shouldUseTariffPrice_whenCustomPriceIsNull() {
        // Arrange
        Long userId = 1L;
        int unlockFee = 5;
        int discount = 10;
        BigDecimal defaultPrice = BigDecimal.valueOf(2.5);
        Duration rentalDuration = Duration.ofMinutes(20);

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Tariff tariff = mock(Tariff.class);
        when(tariff.getTitle()).thenReturn("Silver");
        when(tariff.getPricePerUnit()).thenReturn(defaultPrice);
        when(tariff.getUnlockFee()).thenReturn(unlockFee);

        UserTariff userTariff = mock(UserTariff.class);
        when(userTariff.getTariff()).thenReturn(tariff);
        when(userTariff.getDiscountPct()).thenReturn(discount);
        when(userTariff.getCustomPricePerMinute()).thenReturn(null);

        when(this.userTariffRepository.findActiveTariffByUserAndTime(eq(userId), any(LocalDateTime.class)))
                .thenReturn(Optional.of(userTariff));

        // Act
        BigDecimal result = this.userTariffCostStrategy.calculate(user, tariff, rentalDuration);

        // Assert
        assertEquals(0, result.compareTo(BigDecimal.valueOf(50)));

        verify(this.userTariffRepository, times(1))
                .findActiveTariffByUserAndTime(eq(userId), any(LocalDateTime.class));
    }

    @Test
    void calculate_shouldThrowNotFoundException_ifUserTariffNotFound() {
        // Arrange
        Long userId = 1L;

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Tariff tariff = mock(Tariff.class);

        when(this.userTariffRepository.findActiveTariffByUserAndTime(eq(userId), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.userTariffCostStrategy.calculate(user, tariff, Duration.ofMinutes(10)));
        assertEquals("Special tariff for user 1 not found.", ex.getMessage());

        verify(this.userTariffRepository, times(1))
                .findActiveTariffByUserAndTime(eq(userId), any(LocalDateTime.class));
    }
}