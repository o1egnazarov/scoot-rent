package ru.noleg.scootrent.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.dto.location.CreateLocationDto;
import ru.noleg.scootrent.dto.location.DetailLocationDto;
import ru.noleg.scootrent.dto.location.LocationDto;
import ru.noleg.scootrent.dto.location.ShortLocationDto;
import ru.noleg.scootrent.dto.location.UpdateLocationDto;
import ru.noleg.scootrent.dto.scooter.ScooterDtoWithModel;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.location.LocationType;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.entity.scooter.ScooterModel;
import ru.noleg.scootrent.entity.scooter.ScooterStatus;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationMapperTest {

    @Mock
    ScooterMapper scooterMapper;

    @InjectMocks
    LocationMapperImpl locationMapper;

    @Test
    void mapToEntity_shouldMapCorrectlyWithParent() {
        // Arrange
        CreateLocationDto dto = new CreateLocationDto(
                10L,
                LocationType.CITY,
                "Мира 55а",
                "Центр",
                BigDecimal.valueOf(56.524908),
                BigDecimal.valueOf(84.948017),
                1L
        );

        // Act
        LocationNode entity = this.locationMapper.mapToEntity(dto);

        // Assert
        assertEquals(dto.title(), entity.getTitle());
        assertEquals(dto.locationType(), entity.getLocationType());
        assertEquals(dto.address(), entity.getAddress());
        assertEquals(dto.latitude(), entity.getLatitude());
        assertEquals(dto.longitude(), entity.getLongitude());
        assertNotNull(entity.getParent());
        assertEquals(dto.parentId(), entity.getParent().getId());
    }

    @Test
    void updateRentalPointFromDto_shouldUpdateFieldsAndParent() {
        // Arrange
        LocationNode entity = new LocationNode();
        entity.setParent(new LocationNode());

        UpdateLocationDto dto = new UpdateLocationDto(
                "New address",
                LocationType.DISTRICT,
                "New title",
                BigDecimal.valueOf(56.524908),
                BigDecimal.valueOf(84.948017),
                2L
        );

        // Act
        this.locationMapper.updateRentalPointFromDto(dto, entity);

        // Assert
        assertEquals(dto.address(), entity.getAddress());
        assertEquals(dto.title(), entity.getTitle());
        assertEquals(dto.locationType(), entity.getLocationType());
        assertEquals(dto.latitude(), entity.getLatitude());
        assertEquals(dto.longitude(), entity.getLongitude());
        assertNotNull(entity.getParent());
        assertEquals(dto.parentId(), entity.getParent().getId());
    }

    @Test
    void updateRentalPointFromDto_shouldClearParentIfNull() {
        // Arrange
        LocationNode entity = new LocationNode();
        entity.setParent(new LocationNode());

        UpdateLocationDto dto = new UpdateLocationDto(
                "New address",
                LocationType.COUNTRY,
                "New title",
                null,
                null,
                null
        );

        // Act
        this.locationMapper.updateRentalPointFromDto(dto, entity);

        // Assert
        assertEquals(dto.address(), entity.getAddress());
        assertEquals(dto.title(), entity.getTitle());
        assertEquals(dto.locationType(), entity.getLocationType());
        assertNull(entity.getLatitude());
        assertNull(entity.getLongitude());
        assertNull(entity.getParent());
    }

    @Test
    void mapToDto_shouldMapCorrectly() {
        LocationNode node = new LocationNode();
        node.setId(1L);
        node.setTitle("Центр");
        node.setAddress("ул. Ленина");
        node.setLocationType(LocationType.CITY);
        node.setLatitude(BigDecimal.valueOf(56.524908));
        node.setLongitude(BigDecimal.valueOf(84.948017));

        LocationDto dto = this.locationMapper.mapToDto(node);

        assertEquals(node.getId(), dto.id());
        assertEquals(node.getTitle(), dto.title());
        assertEquals(node.getAddress(), dto.address());
        assertEquals(node.getLocationType(), dto.locationType());
        assertEquals(node.getLatitude(), dto.latitude());
        assertEquals(node.getLongitude(), dto.longitude());
    }

    @Test
    void mapToDetailDto_shouldMapFieldsAndCount() {
        // Arrange
        LocationNode node = new LocationNode();
        node.setId(1L);
        node.setTitle("Парк");
        node.setAddress("ул. Мира 1");
        node.setLocationType(LocationType.RENTAL_POINT);
        node.setLatitude(BigDecimal.valueOf(56.524908));
        node.setLongitude(BigDecimal.valueOf(84.948017));

        Scooter scooter1 = mock(Scooter.class);
        Scooter scooter2 = mock(Scooter.class);
        node.setScooters(Set.of(scooter1, scooter2));

        ScooterDtoWithModel scooterDto1 = new ScooterDtoWithModel(
                1L, "T017PC", ScooterStatus.FREE, Duration.ofHours(1), mock(ScooterModel.class)
        );
        ScooterDtoWithModel scooterDto2 = new ScooterDtoWithModel(
                2L, "T017PC2", ScooterStatus.FREE, Duration.ofHours(1), mock(ScooterModel.class)
        );

        when(scooterMapper.mapToDtoWithModel(scooter1)).thenReturn(scooterDto1);
        when(scooterMapper.mapToDtoWithModel(scooter2)).thenReturn(scooterDto2);

        // Act
        DetailLocationDto dto = this.locationMapper.mapToDetailDto(node);

        // Assert
        assertEquals(node.getId(), dto.id());
        assertEquals(node.getTitle(), dto.title());
        assertEquals(node.getAddress(), dto.address());
        assertEquals(2, dto.totalCount());
        assertEquals(node.getLatitude(), dto.latitude());
        assertEquals(node.getLongitude(), dto.longitude());
        assertEquals(node.getLocationType(), dto.locationType());
    }

    @Test
    void mapToShortDto_shouldMapBasicFieldsOnly() {
        LocationNode node = new LocationNode();
        node.setId(5L);
        node.setTitle("Парк Горького");
        node.setLocationType(LocationType.RENTAL_POINT);
        node.setAddress("ул. Горького");

        ShortLocationDto dto = this.locationMapper.mapToShortDto(node);

        assertEquals(node.getId(), dto.id());
        assertEquals(node.getTitle(), dto.title());
        assertEquals(node.getLocationType(), dto.locationType());
        assertEquals(node.getAddress(), dto.address());
    }
}