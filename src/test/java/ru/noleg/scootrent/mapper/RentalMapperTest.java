package ru.noleg.scootrent.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.dto.location.ShortLocationDto;
import ru.noleg.scootrent.dto.rental.ShortRentalDto;
import ru.noleg.scootrent.dto.scooter.ShortScooterDtoWithModel;
import ru.noleg.scootrent.dto.tariff.ShortTariffDto;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.location.LocationType;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.rental.RentalStatus;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.entity.scooter.ScooterStatus;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.entity.user.User;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalMapperTest {
    @InjectMocks
    private RentalMapperImpl rentalMapper;

    @Mock
    private ScooterMapper scooterMapper;

    @Mock
    private TariffMapper tariffMapper;

    @Mock
    private LocationMapper locationMapper;

    @Test
    void mapToDto_shouldMapAllFieldsCorrectly() {
        // Arrange
        Rental rental = this.getRental();

        ShortScooterDtoWithModel scooterDto =
                new ShortScooterDtoWithModel(2L, "T017PC", ScooterStatus.FREE, "Xiaomi scoot pro");

        ShortTariffDto tariffDto = new ShortTariffDto(
                4L,
                "Silver",
                BigDecimal.valueOf(10),
                5,
                TariffType.SPECIAL_TARIFF,
                BillingMode.PER_HOUR,
                10,
                LocalDateTime.of(2025, 1, 1, 1, 1),
                LocalDateTime.of(2025, 7, 7, 7, 7)
        );

        ShortLocationDto startPointDto = new ShortLocationDto(5L, "Москва", LocationType.RENTAL_POINT, "Тверская 1");

        ShortLocationDto endPointDto = new ShortLocationDto(6L, "Москва", LocationType.CITY, "Арбат 5");

        when(this.scooterMapper.mapToShortDtoWithModel(rental.getScooter())).thenReturn(scooterDto);
        when(this.tariffMapper.mapToShortDto(rental.getTariff())).thenReturn(tariffDto);
        when(this.locationMapper.mapToShortDto(rental.getStartPoint())).thenReturn(startPointDto);
        when(this.locationMapper.mapToShortDto(rental.getEndPoint())).thenReturn(endPointDto);

        // Act
        ShortRentalDto dto = this.rentalMapper.mapToDto(rental);

        // Assert
        assertEquals(rental.getId(), dto.id());
        assertEquals(scooterDto, dto.scooter());
        assertEquals(tariffDto, dto.tariff());
        assertEquals(startPointDto, dto.startPoint());
        assertEquals(endPointDto, dto.endPoint());
        assertEquals(rental.getRentalStatus(), dto.rentalStatus());
        assertEquals(rental.getCost(), dto.cost());
        assertEquals(rental.getDurationOfRental(), dto.durationOfRental());
        assertEquals(rental.getStartTime(), dto.startTime());
        assertEquals(rental.getEndTime(), dto.endTime());
        assertEquals(rental.getDurationInPause(), dto.durationInPause());
    }

    private Rental getRental() {
        Rental rental = new Rental();
        rental.setId(1L);

        User user = new User();
        user.setId(1L);
        rental.setUser(user);

        Scooter scooter = new Scooter();
        scooter.setId(1L);
        rental.setScooter(scooter);

        Tariff tariff = new Tariff();
        tariff.setId(1L);
        rental.setTariff(tariff);

        LocationNode locationNode1 = new LocationNode();
        locationNode1.setId(1L);
        rental.setStartPoint(locationNode1);

        LocationNode locationNode2 = new LocationNode();
        locationNode2.setId(2L);
        rental.setEndPoint(locationNode2);

        rental.setRentalStatus(RentalStatus.ACTIVE);
        rental.setCost(BigDecimal.valueOf(123.45));
        rental.setDurationOfRental(Duration.ofMinutes(30));
        rental.setStartTime(LocalDateTime.of(2025, 5, 1, 12, 0));
        rental.setEndTime(LocalDateTime.of(2025, 5, 1, 12, 30));
        rental.setDurationInPause(Duration.ofMinutes(5));
        return rental;
    }
}