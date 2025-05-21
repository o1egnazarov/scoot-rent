package ru.noleg.scootrent.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.noleg.scootrent.dto.rental.ScooterRentalHistoryDto;
import ru.noleg.scootrent.dto.rental.UserRentalHistoryDto;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.user.User;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RentalHistoryMapperTest {
    private final RentalHistoryMapper rentalHistoryMapper = Mappers.getMapper(RentalHistoryMapper.class);

    @Test
    void mapToScooterRentalDto_shouldMapCorrectly() {
        // Arrange
        Rental rental = this.getRental();

        // Act
        ScooterRentalHistoryDto dto = this.rentalHistoryMapper.mapToScooterRentalDto(rental);

        // Assert
        assertEquals(rental.getId(), dto.rentalId());
        assertEquals(rental.getUser().getUsername(), dto.username());
        assertEquals(rental.getUser().getPhone(), dto.phone());
        assertEquals(rental.getStartPoint().getTitle(), dto.startPointTitle());
        assertEquals(rental.getEndPoint().getTitle(), dto.endPointTitle());
        assertEquals(rental.getTariff().getTitle(), dto.tariffTitle());
        assertEquals(rental.getScooter().getDurationInUsed(), dto.durationOfUsedScooter());
    }

    @Test
    void mapToUserRentalDto_shouldMapCorrectly() {
        // Arrange
        Rental rental = this.getRental();

        // Act
        UserRentalHistoryDto dto = this.rentalHistoryMapper.mapToUserRentalDto(rental);

        // Assert
        assertEquals(rental.getId(), dto.rentalId());
        assertEquals(rental.getStartPoint().getTitle(), dto.startPointTitle());
        assertEquals(rental.getEndPoint().getTitle(), dto.endPointTitle());
        assertEquals(rental.getTariff().getTitle(), dto.tariffTitle());
        assertEquals(rental.getDurationOfRental(), dto.duration());
    }

    private Rental getRental() {
        User user = new User();
        user.setUsername("username");
        user.setPhone("phoneNumber");

        LocationNode start = new LocationNode();
        start.setTitle("Start Point");

        LocationNode end = new LocationNode();
        end.setTitle("End Point");

        Tariff tariff = new Tariff();
        tariff.setTitle("Silver");

        Scooter scooter = new Scooter();
        scooter.setDurationInUsed(Duration.ofMinutes(30));

        Rental rental = new Rental();
        rental.setId(1L);
        rental.setUser(user);
        rental.setStartPoint(start);
        rental.setEndPoint(end);
        rental.setTariff(tariff);
        rental.setScooter(scooter);
        return rental;
    }
}