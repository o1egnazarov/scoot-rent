package ru.noleg.scootrent.service.rental.billing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.service.rental.billing.strategy.RentalCostStrategy;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingServiceImplTest {
    @Mock
    private RentalCostStrategy defaultTariffStrategy;

    @Mock
    private RentalCostStrategy subscriptionStrategy;

    private BillingServiceImpl billingService;

    @BeforeEach
    void setUp() {
        when(this.defaultTariffStrategy.getSupportedTariffType()).thenReturn(TariffType.DEFAULT_TARIFF);
        when(this.subscriptionStrategy.getSupportedTariffType()).thenReturn(TariffType.SUBSCRIPTION);

        this.billingService = new BillingServiceImpl(List.of(defaultTariffStrategy, subscriptionStrategy));
    }

    @Test
    void calculateRentalCost_shouldUseCorrectStrategy_whenTariffTypeIsSupported() {
        // Arrange
        Duration duration = Duration.ofHours(2);
        BigDecimal expectedCost = new BigDecimal("150.00");

        User user = mock(User.class);
        Tariff tariff = mock(Tariff.class);
        when(tariff.getType()).thenReturn(TariffType.DEFAULT_TARIFF);
        when(this.defaultTariffStrategy.calculate(user, tariff, duration)).thenReturn(expectedCost);

        // Act
        BigDecimal result = this.billingService.calculateRentalCost(user, tariff, duration);

        // Assert
        assertEquals(expectedCost, result);
        verify(this.defaultTariffStrategy, times(1)).calculate(user, tariff, duration);
        verify(this.subscriptionStrategy, never()).calculate(any(), any(), any());
    }

    @Test
    void calculateRentalCost_shouldThrowException_whenStrategyNotFound() {
        // Arrange
        Duration duration = Duration.ofHours(2);
        User user = mock(User.class);
        Tariff tariff = mock(Tariff.class);

        when(tariff.getType()).thenReturn(TariffType.SPECIAL_TARIFF);

        // Act | Assert
        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> this.billingService.calculateRentalCost(user, tariff, duration)
        );

        assertEquals("Unsupported tariff type: SPECIAL_TARIFF", exception.getMessage());

        verify(this.defaultTariffStrategy, never()).calculate(user, tariff, duration);
        verify(this.subscriptionStrategy, never()).calculate(any(), any(), any());
    }

    @Test
    void constructor_shouldThrowException_whenDuplicateStrategiesExist() {
        // Arrange
        Duration duration = Duration.ofHours(2);
        User user = mock(User.class);
        Tariff tariff = mock(Tariff.class);

        RentalCostStrategy duplicate = mock(RentalCostStrategy.class);

        when(duplicate.getSupportedTariffType()).thenReturn(TariffType.DEFAULT_TARIFF);
        when(this.defaultTariffStrategy.getSupportedTariffType()).thenReturn(TariffType.DEFAULT_TARIFF);

        // Act | Assert
        BusinessLogicException exception = assertThrows(BusinessLogicException.class,
                () -> new BillingServiceImpl(List.of(defaultTariffStrategy, duplicate))
        );

        assertEquals("Duplicate strategy for tariff type: DEFAULT_TARIFF", exception.getMessage());

        verify(this.defaultTariffStrategy, never()).calculate(user, tariff, duration);
        verify(this.subscriptionStrategy, never()).calculate(any(), any(), any());
    }
}