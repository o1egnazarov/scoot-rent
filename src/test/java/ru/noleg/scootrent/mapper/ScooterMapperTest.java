package ru.noleg.scootrent.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.noleg.scootrent.dto.scooter.ScooterDto;
import ru.noleg.scootrent.dto.scooter.ScooterDtoWithModel;
import ru.noleg.scootrent.dto.scooter.ShortScooterDtoWithModel;
import ru.noleg.scootrent.dto.scooter.UpdateScooterDto;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.entity.scooter.ScooterModel;
import ru.noleg.scootrent.entity.scooter.ScooterStatus;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScooterMapperTest {
    private final ScooterMapper scooterMapper = Mappers.getMapper(ScooterMapper.class);

    @Test
    void mapToDto_shouldMapCorrectly() {
        // Arrange
        ScooterModel model = new ScooterModel();
        model.setId(1L);

        LocationNode rentalPoint = new LocationNode();
        rentalPoint.setId(2L);

        Scooter scooter = new Scooter();
        scooter.setId(3L);
        scooter.setNumberPlate("T017PC");
        scooter.setStatus(ScooterStatus.FREE);
        scooter.setDurationInUsed(Duration.ofMinutes(30));
        scooter.setModel(model);
        scooter.setRentalPoint(rentalPoint);

        // Act
        ScooterDto dto = this.scooterMapper.mapToDto(scooter);

        // Assert
        assertEquals(scooter.getId(), dto.id());
        assertEquals(scooter.getNumberPlate(), dto.numberPlate());
        assertEquals(scooter.getStatus(), dto.status());
        assertEquals(scooter.getDurationInUsed(), dto.durationInUsed());
        assertEquals(model.getId(), dto.modelId());
        assertEquals(rentalPoint.getId(), dto.rentalPointId());
    }

    @Test
    void mapToEntity_shouldMapCorrectly() {
        // Arrange
        ScooterDto dto = new ScooterDto(
                3L,
                "T017PC",
                ScooterStatus.TAKEN,
                Duration.ofMinutes(45),
                1L,
                2L
        );

        // Act
        Scooter entity = this.scooterMapper.mapToEntity(dto);

        // Assert
        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.numberPlate(), entity.getNumberPlate());
        assertEquals(dto.status(), entity.getStatus());
        assertEquals(dto.durationInUsed(), entity.getDurationInUsed());
        assertEquals(dto.modelId(), entity.getModel().getId());
        assertEquals(dto.rentalPointId(), entity.getRentalPoint().getId());
    }

    @Test
    void mapToDtoWithModel_shouldMapCorrectly() {
        // Arrange
        ScooterModel model = new ScooterModel();
        model.setId(1L);
        model.setTitle("Xiaomi 3 Pro Max Ultra 64gb");

        Scooter scooter = new Scooter();
        scooter.setId(1L);
        scooter.setNumberPlate("T917PC");
        scooter.setStatus(ScooterStatus.FREE);
        scooter.setDurationInUsed(Duration.ofMinutes(10));
        scooter.setModel(model);

        // Act
        ScooterDtoWithModel dto = this.scooterMapper.mapToDtoWithModel(scooter);

        // Assert
        assertEquals(scooter.getId(), dto.id());
        assertEquals(scooter.getNumberPlate(), dto.numberPlate());
        assertEquals(scooter.getStatus(), dto.status());
        assertEquals(scooter.getDurationInUsed(), dto.durationInUsed());
        assertEquals(model, dto.model());
    }

    @Test
    void mapToShortDtoWithModel_shouldMapCorrectly() {
        // Arrange
        ScooterModel model = new ScooterModel();
        model.setTitle("Xiaomi 3 Pro Max Ultra 64gb");

        Scooter scooter = new Scooter();
        scooter.setNumberPlate("T017PC");
        scooter.setModel(model);

        // Act
        ShortScooterDtoWithModel dto = this.scooterMapper.mapToShortDtoWithModel(scooter);

        // Assert
        assertEquals(scooter.getNumberPlate(), dto.numberPlate());
        assertEquals(model.getTitle(), dto.modelTitle());
    }

    @Test
    void updateScooterFromDto_shouldUpdateEntityFields() {
        // Arrange
        Scooter scooter = new Scooter();
        scooter.setNumberPlate("OLDT017PC");
        scooter.setStatus(ScooterStatus.FREE);
        scooter.setDurationInUsed(Duration.ofMinutes(5));
        scooter.setModel(new ScooterModel());
        scooter.setRentalPoint(new LocationNode());

        UpdateScooterDto dto = new UpdateScooterDto(
                "NEWT017PC",
                ScooterStatus.TAKEN,
                Duration.ofMinutes(90),
                111L,
                222L
        );

        // Act
        this.scooterMapper.updateScooterFromDto(dto, scooter);

        // Assert
        assertEquals(dto.numberPlate(), scooter.getNumberPlate());
        assertEquals(dto.status(), scooter.getStatus());
        assertEquals(dto.durationInUsed(), scooter.getDurationInUsed());
        assertEquals(dto.modelId(), scooter.getModel().getId());
        assertEquals(dto.rentalPointId(), scooter.getRentalPoint().getId());
    }
}