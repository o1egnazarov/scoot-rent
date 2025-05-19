package ru.noleg.scootrent.service.rental.tariffselect.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.TariffRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultTariffStrategyTest {
    @Mock
    private TariffRepository tariffRepository;

    @InjectMocks
    DefaultTariffStrategy defaultTariffStrategy;

    private static final int LOWEST_PRIORITY = 100;

    @Test
    void getPriority_shouldReturnLowestPriority() {
        // Act
        int result = this.defaultTariffStrategy.getPriority();

        // Assert
        assertEquals(LOWEST_PRIORITY, result);
    }

    @Test
    void selectTariff_shouldReturnDefaultTariff_whenFound() {
        // Arrange
        Long userId = 1L;
        BillingMode billingMode = BillingMode.PER_HOUR;
        Tariff tariff = mock(Tariff.class);

        when(this.tariffRepository.findDefaultTariffByBillingMode(billingMode)).thenReturn(Optional.of(tariff));

        // Act
        Tariff result = this.defaultTariffStrategy.selectTariff(userId, billingMode);

        // Assert
        assertEquals(tariff, result);

        verify(this.tariffRepository, times(1)).findDefaultTariffByBillingMode(billingMode);
    }

    @Test
    void selectTariff_shouldThrowNotFoundException_whenDefaultTariffNotFound() {
        // Arrange
        Long userId = 1L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        when(this.tariffRepository.findDefaultTariffByBillingMode(billingMode)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.defaultTariffStrategy.selectTariff(userId, billingMode)
        );

        assertEquals("Default tariff not found.", ex.getMessage());

        verify(this.tariffRepository, times(1)).findDefaultTariffByBillingMode(billingMode);
    }
}