package ru.noleg.scootrent.service.rental.tariffselect;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.TariffSelectionException;
import ru.noleg.scootrent.service.rental.tariffselect.strategy.TariffSelectionStrategy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TariffSelectionServiceImplTest {

    @Test
    void selectTariffForUser_shouldReturnFirstNonNullTariff() {
        // Arrange
        Long userId = 1L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        Tariff expectedTariff = mock(Tariff.class);
        when(expectedTariff.getTitle()).thenReturn("Default tariff");
        when(expectedTariff.getType()).thenReturn(TariffType.DEFAULT_TARIFF);

        TariffSelectionStrategy strategy1 = mock(TariffSelectionStrategy.class);
        TariffSelectionStrategy strategy2 = mock(TariffSelectionStrategy.class);

        when(strategy1.getPriority()).thenReturn(2);
        when(strategy2.getPriority()).thenReturn(1);

        lenient().when(strategy1.selectTariff(userId, billingMode)).thenReturn(null);
        when(strategy2.selectTariff(userId, billingMode)).thenReturn(expectedTariff);

        TariffSelectionServiceImpl service = new TariffSelectionServiceImpl(List.of(strategy1, strategy2));

        // Act
        Tariff result = service.selectTariffForUser(userId, billingMode);

        // Assert
        assertEquals(expectedTariff, result);
        verify(strategy2, times(1)).selectTariff(userId, billingMode);
        verify(strategy1, never()).selectTariff(userId, billingMode);
    }

    @Test
    void selectTariffForUser_shouldThrowTariffSelectionException_whenStrategyFails() {
        // Arrange
        Long userId = 2L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        TariffSelectionStrategy strategy = mock(TariffSelectionStrategy.class);
        when(strategy.selectTariff(userId, billingMode)).thenThrow(new RuntimeException("Unexpected failure"));

        TariffSelectionServiceImpl service = new TariffSelectionServiceImpl(List.of(strategy));

        // Act | Assert
        TariffSelectionException ex = assertThrows(TariffSelectionException.class, () ->
                service.selectTariffForUser(userId, billingMode)
        );

        assertEquals("Error on selection tariff.", ex.getMessage());
        verify(strategy, times(1)).selectTariff(userId, billingMode);
    }

    @Test
    void selectTariffForUser_shouldThrowBusinessLogicException_whenNoStrategyReturnsTariff() {
        // Arrange
        Long userId = 1L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        TariffSelectionStrategy strategy1 = mock(TariffSelectionStrategy.class);
        TariffSelectionStrategy strategy2 = mock(TariffSelectionStrategy.class);

        when(strategy1.getPriority()).thenReturn(1);
        when(strategy2.getPriority()).thenReturn(2);
        when(strategy1.selectTariff(userId, billingMode)).thenReturn(null);
        when(strategy2.selectTariff(userId, billingMode)).thenReturn(null);

        TariffSelectionServiceImpl service = new TariffSelectionServiceImpl(List.of(strategy1, strategy2));

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () ->
                service.selectTariffForUser(userId, billingMode)
        );

        assertEquals("No tariff found for user with id: 1", ex.getMessage());
        verify(strategy1, times(1)).selectTariff(userId, billingMode);
        verify(strategy2, times(1)).selectTariff(userId, billingMode);
    }
}