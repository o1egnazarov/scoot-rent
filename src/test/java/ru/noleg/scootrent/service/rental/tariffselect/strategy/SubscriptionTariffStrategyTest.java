package ru.noleg.scootrent.service.rental.tariffselect.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.tariff.UserSubscription;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.repository.UserSubscriptionRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionTariffStrategyTest {
    @Mock
    private UserSubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionTariffStrategy subscriptionTariffStrategy;

    private static final int HIGHEST_PRIORITY = 1;

    @Test
    void getPriority_shouldReturnHighestPriority() {
        // Act
        int result = this.subscriptionTariffStrategy.getPriority();

        // Assert
        assertEquals(HIGHEST_PRIORITY, result);
    }

    @Test
    void selectTariff_shouldReturnTariff_whenActiveSubscriptionExists() {
        // Arrange
        Long userId = 1L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        Tariff tariff = mock(Tariff.class);
        UserSubscription subscription = mock(UserSubscription.class);

        when(subscription.getTariff()).thenReturn(tariff);
        when(this.subscriptionRepository.findActiveSubscriptionByUserAndTime(eq(userId), any(LocalDateTime.class)))
                .thenReturn(Optional.of(subscription));

        // Act
        Tariff result = this.subscriptionTariffStrategy.selectTariff(userId, billingMode);

        // Assert
        assertEquals(tariff, result);

        verify(this.subscriptionRepository, times(1))
                .findActiveSubscriptionByUserAndTime(eq(userId), any(LocalDateTime.class));
        verify(subscription, times(1)).getTariff();
    }

    @Test
    void selectTariff_shouldReturnNull_whenNoActiveSubscription() {
        // Arrange
        Long userId = 100L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        when(this.subscriptionRepository.findActiveSubscriptionByUserAndTime(eq(userId), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        // Act
        Tariff result = this.subscriptionTariffStrategy.selectTariff(userId, billingMode);

        // Assert
        assertNull(result);

        verify(this.subscriptionRepository, times(1))
                .findActiveSubscriptionByUserAndTime(eq(userId), any(LocalDateTime.class));
    }
}