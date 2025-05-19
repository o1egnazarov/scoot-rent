package ru.noleg.scootrent.service.rental.tariffselect.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.UserTariff;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.repository.UserTariffRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTariffStrategyTest {

    private static final int PRIORITY = 2;

    @Mock
    private UserTariffRepository userTariffRepository;

    @InjectMocks
    UserTariffStrategy userTariffStrategy;

    @Test
    void getPriority_shouldReturnPriority() {
        // Act
        int result = this.userTariffStrategy.getPriority();

        // Assert
        assertEquals(PRIORITY, result);
    }

    @Test
    void selectTariff_shouldReturnTariff_whenActiveUserTariffExists() {
        // Arrange
        Long userId = 1L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        Tariff tariff = mock(Tariff.class);
        UserTariff subscription = mock(UserTariff.class);

        when(subscription.getTariff()).thenReturn(tariff);
        when(this.userTariffRepository.findActiveTariffByUserAndTime(eq(userId), any(LocalDateTime.class)))
                .thenReturn(Optional.of(subscription));

        // Act
        Tariff result = this.userTariffStrategy.selectTariff(userId, billingMode);

        // Assert
        assertEquals(tariff, result);

        verify(this.userTariffRepository, times(1))
                .findActiveTariffByUserAndTime(eq(userId), any(LocalDateTime.class));
        verify(subscription, times(1)).getTariff();
    }

    @Test
    void selectTariff_shouldReturnNull_whenNoActiveUserTariff() {
        // Arrange
        Long userId = 100L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        when(this.userTariffRepository.findActiveTariffByUserAndTime(eq(userId), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        // Act
        Tariff result = this.userTariffStrategy.selectTariff(userId, billingMode);

        // Assert
        assertNull(result);

        verify(this.userTariffRepository, times(1))
                .findActiveTariffByUserAndTime(eq(userId), any(LocalDateTime.class));
    }

}