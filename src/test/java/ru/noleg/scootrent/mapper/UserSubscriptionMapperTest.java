package ru.noleg.scootrent.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.noleg.scootrent.dto.tariff.UserSubscriptionDto;
import ru.noleg.scootrent.entity.tariff.UserSubscription;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserSubscriptionMapperTest {

    private final UserSubscriptionMapper userSubscriptionMapper = Mappers.getMapper(UserSubscriptionMapper.class);

    @Test
    void mapToDto_shouldMapCorrectly() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Tariff tariff = new Tariff();
        tariff.setId(10L);

        UserSubscription entity = new UserSubscription(
                1L,
                user,
                tariff,
                100,
                10,
                LocalDateTime.of(2025, 1, 1, 1, 1),
                LocalDateTime.of(2025, 1, 1, 1, 1)
        );

        entity.setTariff(tariff);

        // Act
        UserSubscriptionDto dto = this.userSubscriptionMapper.mapToDto(entity);

        // Assert
        assertEquals(entity.getId(), dto.id());
        assertEquals(tariff.getId(), dto.tariffId());
        assertEquals(entity.getMinuteUsageLimit(), dto.minuteUsageLimit());
        assertEquals(entity.getMinutesUsed(), dto.minutesUsed());
        assertEquals(entity.getStartDate(), dto.startDate());
        assertEquals(entity.getEndDate(), dto.endDate());
    }
}