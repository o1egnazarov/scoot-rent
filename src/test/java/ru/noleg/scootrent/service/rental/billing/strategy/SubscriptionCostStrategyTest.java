package ru.noleg.scootrent.service.rental.billing.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.noleg.scootrent.entity.UserSubscription;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.CostCalculationException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.UserSubscriptionRepository;

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
class SubscriptionCostStrategyTest {

    @Mock
    private UserSubscriptionRepository userSubscriptionRepository;

    @InjectMocks
    private SubscriptionCostStrategy subscriptionCostStrategy;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(subscriptionCostStrategy, "extraPricePerMinute", BigDecimal.valueOf(2.5));
    }


    @Test
    void getSupportedTariffType_shouldReturnSubscriptionTariff() {
        assertEquals(TariffType.SUBSCRIPTION, this.subscriptionCostStrategy.getSupportedTariffType());
    }

    @Test
    void calculate_shouldReturnZeroCost_whenUsageWithinLimit() {
        // Arrange
        Long userId = 1L;
        int limit = 100;
        int used = 50;
        Duration rentalDuration = Duration.ofMinutes(30);

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Tariff tariff = mock(Tariff.class);
        UserSubscription subscription = mock(UserSubscription.class);

        when(subscription.getTariff()).thenReturn(tariff);
        when(tariff.getTitle()).thenReturn("Gold");

        when(subscription.getMinuteUsageLimit()).thenReturn(limit);
        when(subscription.getMinutesUsed()).thenReturn(used);

        when(this.userSubscriptionRepository.findActiveSubscriptionByUserAndTime(eq(userId), any(LocalDateTime.class)))
                .thenReturn(Optional.of(subscription));

        // Act
        BigDecimal cost = this.subscriptionCostStrategy.calculate(user, tariff, rentalDuration);

        // Assert
        assertEquals(BigDecimal.ZERO, cost);
        verify(subscription, times(1)).addMinutes(30);
        verify(this.userSubscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void calculate_shouldReturnCost_whenOverusedMinutes() {
        // Arrange
        Long userId = 1L;
        int limit = 50;
        int used = 45;
        Duration rentalDuration = Duration.ofMinutes(10);

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Tariff tariff = mock(Tariff.class);
        UserSubscription subscription = mock(UserSubscription.class);

        when(subscription.getTariff()).thenReturn(tariff);
        when(tariff.getTitle()).thenReturn("Premium");
        when(subscription.getMinuteUsageLimit()).thenReturn(limit);
        when(subscription.getMinutesUsed()).thenReturn(used);

        when(this.userSubscriptionRepository.findActiveSubscriptionByUserAndTime(eq(userId), any(LocalDateTime.class)))
                .thenReturn(Optional.of(subscription));

        // Act
        BigDecimal cost = this.subscriptionCostStrategy.calculate(user, tariff, rentalDuration);

        // Assert
        assertEquals(BigDecimal.valueOf(5).multiply(BigDecimal.valueOf(2.5)), cost);
        verify(subscription, times(1)).addMinutes(10);
        verify(this.userSubscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void calculate_shouldThrowNotFoundException_whenSubscriptionNotFound() {
        // Arrange
        Long userId = 1L;
        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Tariff tariff = mock(Tariff.class);

        when(this.userSubscriptionRepository.findActiveSubscriptionByUserAndTime(eq(userId), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.subscriptionCostStrategy.calculate(user, tariff, Duration.ofMinutes(10)));

        assertEquals("Subscription for user 1 not found.", ex.getMessage());
        verify(this.userSubscriptionRepository, never()).save(any(UserSubscription.class));
    }

    @Test
    void calculate_shouldThrowCostCalculationException_whenExceptionOccurs() {
        // Arrange
        Long userId = 1L;
        int limit = 50;
        int used = 45;
        Duration rentalDuration = Duration.ofMinutes(10);

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Tariff tariff = mock(Tariff.class);
        when(tariff.getTitle()).thenReturn("ErrorTariff");

        UserSubscription subscription = mock(UserSubscription.class);
        when(subscription.getTariff()).thenReturn(tariff);
        when(subscription.getMinuteUsageLimit()).thenReturn(limit);
        when(subscription.getMinutesUsed()).thenReturn(used);

        ReflectionTestUtils.setField(this.subscriptionCostStrategy, "extraPricePerMinute", null);

        when(this.userSubscriptionRepository.findActiveSubscriptionByUserAndTime(eq(userId), any()))
                .thenReturn(Optional.of(subscription));

        // Act | Assert
        CostCalculationException ex = assertThrows(CostCalculationException.class,
                () -> this.subscriptionCostStrategy.calculate(user, tariff, rentalDuration));

        assertEquals(ex.getMessage(), "Error calculation cost for tariff ErrorTariff");
    }
}