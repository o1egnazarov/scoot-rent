package ru.noleg.scootrent.service.rental.billing.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.CostCalculationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultTariffCostStrategyTest {

    private DefaultTariffCostStrategy defaultTariffCostStrategy;

    @BeforeEach
    void setUp() {
        this.defaultTariffCostStrategy = new DefaultTariffCostStrategy();

    }

    @Test
    void getSupportedTariffType_shouldReturnDefaultTariff() {
        assertEquals(TariffType.DEFAULT_TARIFF, this.defaultTariffCostStrategy.getSupportedTariffType());
    }

    @Test
    void calculate_shouldCalculateCorrectly_whenPerMinuteBillingAndShortRide() {
        // Arrange
        Duration duration = Duration.ofMinutes(5);
        BigDecimal pricePerMinute = BigDecimal.valueOf(3.0);
        int unlockFee = 25;

        User user = mock(User.class);
        Tariff tariff = mock(Tariff.class);

        when(tariff.getTitle()).thenReturn("Standard Tariff");
        when(tariff.getUnlockFee()).thenReturn(unlockFee);
        when(tariff.getBillingMode()).thenReturn(BillingMode.PER_MINUTE);
        when(tariff.getPricePerUnit()).thenReturn(pricePerMinute);

        // Act
        BigDecimal cost = defaultTariffCostStrategy.calculate(user, tariff, duration);

        // Assert
        assertEquals(BigDecimal.valueOf(40.0), cost);
    }

    @Test
    void calculate_shouldApplyShortRideSurcharge_whenPerHourBillingAndShortRide() {
        // Arrange
        BigDecimal shortRideSurcharge = BigDecimal.valueOf(1.2);
        Duration duration = Duration.ofMinutes(3);
        BigDecimal pricePerUnit = BigDecimal.valueOf(200);
        int unlockFee = 40;

        User user = mock(User.class);
        Tariff tariff = mock(Tariff.class);

        when(tariff.getTitle()).thenReturn("Standard Tariff");
        when(tariff.getUnlockFee()).thenReturn(unlockFee);
        when(tariff.getBillingMode()).thenReturn(BillingMode.PER_HOUR);
        when(tariff.getPricePerUnit()).thenReturn(pricePerUnit);

        BigDecimal durationInHours = BigDecimal.valueOf(duration.toMinutes()).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        System.out.println("Duration in hour: " + durationInHours);
        BigDecimal expectedBase = pricePerUnit.multiply(durationInHours);
        System.out.println("Expected base price: " + expectedBase);
        BigDecimal expectedSurcharge = expectedBase.multiply(shortRideSurcharge);
        System.out.println("Expected base price with surcharge: " + expectedSurcharge);
        BigDecimal expectedTotal = expectedSurcharge.add(BigDecimal.valueOf(unlockFee));
        System.out.println("Total cost: " + expectedTotal);

        // Act
        BigDecimal cost = this.defaultTariffCostStrategy.calculate(user, tariff, duration);

        // Assert
        assertEquals(expectedTotal, cost);
    }

    @Test
    void calculate_shouldCalculateCorrectly_whenPerMinuteBillingAndLongRide() {
        // Arrange
        Duration duration = Duration.ofMinutes(45);
        BigDecimal pricePerMinute = BigDecimal.valueOf(3.0);
        int unlockFee = 25;

        User user = mock(User.class);
        Tariff tariff = mock(Tariff.class);

        when(tariff.getTitle()).thenReturn("Standard Tariff");
        when(tariff.getBillingMode()).thenReturn(BillingMode.PER_MINUTE);
        when(tariff.getPricePerUnit()).thenReturn(pricePerMinute);
        when(tariff.getUnlockFee()).thenReturn(unlockFee);

        // Act
        BigDecimal cost = defaultTariffCostStrategy.calculate(user, tariff, duration);

        // Assert
        assertEquals(BigDecimal.valueOf(160.0), cost);
    }

    @Test
    void calculate_shouldApplyLongRideDiscount_whenPerHourBillingAndLongRide() {
        // Arrange
        BigDecimal longRideDiscount = BigDecimal.valueOf(0.8);
        Duration duration = Duration.ofMinutes(45);
        BigDecimal pricePerHour = BigDecimal.valueOf(200);
        int unlockFee = 25;

        User user = mock(User.class);
        Tariff tariff = mock(Tariff.class);

        when(tariff.getTitle()).thenReturn("Standard Tariff");
        when(tariff.getUnlockFee()).thenReturn(unlockFee);
        when(tariff.getBillingMode()).thenReturn(BillingMode.PER_HOUR);
        when(tariff.getPricePerUnit()).thenReturn(pricePerHour);

        BigDecimal durationInHours = BigDecimal.valueOf(duration.toMinutes()).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        System.out.println("Duration in hour: " + durationInHours);
        BigDecimal expectedBase = pricePerHour.multiply(durationInHours);
        System.out.println("Expected base price: " + expectedBase);
        BigDecimal expectedSurcharge = expectedBase.multiply(longRideDiscount);
        System.out.println("Expected base price with surcharge: " + expectedSurcharge);
        BigDecimal expectedTotal = expectedSurcharge.add(BigDecimal.valueOf(unlockFee));
        System.out.println("Total cost: " + expectedTotal);

        // Act
        BigDecimal cost = this.defaultTariffCostStrategy.calculate(user, tariff, duration);

        // Assert
        assertEquals(expectedTotal, cost);
    }

    @Test
    void calculate_shouldWrapExceptionInCostCalculationException_whenInternalErrorOccurs() {
        // Arrange
        User user = mock(User.class);
        Tariff tariff = mock(Tariff.class);

        when(tariff.getPricePerUnit()).thenThrow(new NullPointerException("Tariff is null"));


        // Act | Assert
        CostCalculationException exception = assertThrows(CostCalculationException.class,
                () -> this.defaultTariffCostStrategy.calculate(user, tariff, Duration.ofMinutes(10))
        );

        assertTrue(exception.getMessage().contains("Error calculation cost for user with id:"));
    }
}