package ru.noleg.scootrent.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.noleg.scootrent.dto.tariff.UserTariffDto;
import ru.noleg.scootrent.entity.tariff.UserTariff;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTariffMapperTest {

    private final UserTariffMapper userTariffMapper = Mappers.getMapper(UserTariffMapper.class);

    @Test
    void mapToDto_shouldMapCorrectly() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Tariff tariff = new Tariff();
        tariff.setId(10L);

        UserTariff entity = new UserTariff(
                1L,
                user,
                tariff,
                10,
                BigDecimal.valueOf(2.5),
                LocalDateTime.of(2025, 1, 1, 1, 1),
                LocalDateTime.of(2025, 1, 1, 1, 1)
        );

        entity.setTariff(tariff);

        // Act
        UserTariffDto dto = this.userTariffMapper.mapToDto(entity);

        // Assert
        assertEquals(entity.getId(), dto.id());
        assertEquals(tariff.getId(), dto.tariffId());
        assertEquals(entity.getCustomPricePerMinute(), dto.customPricePerMinute());
        assertEquals(entity.getDiscountPct(), dto.discountPct());
        assertEquals(entity.getStartDate(), dto.startDate());
        assertEquals(entity.getEndDate(), dto.endDate());
    }
}