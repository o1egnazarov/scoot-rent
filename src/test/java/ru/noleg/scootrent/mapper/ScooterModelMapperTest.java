package ru.noleg.scootrent.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.noleg.scootrent.dto.scooter.scootermodel.ScooterModelDto;
import ru.noleg.scootrent.dto.scooter.scootermodel.UpdateScooterModelDto;
import ru.noleg.scootrent.entity.scooter.ScooterModel;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScooterModelMapperTest {
    private final ScooterModelMapper scooterModelMapper = Mappers.getMapper(ScooterModelMapper.class);

    @Test
    void mapToDto_shouldMapCorrectly() {
        // Arrange
        ScooterModel entity = new ScooterModel(
                1L,
                "Xiaomi mi scoot",
                25,
                40
        );

        // Act
        ScooterModelDto dto = this.scooterModelMapper.mapToDto(entity);

        // Assert
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getTitle(), dto.title());
        assertEquals(entity.getMaxSpeed(), dto.maxSpeed());
        assertEquals(entity.getRangeKm(), dto.rangeKm());
    }

    @Test
    void mapToEntity_shouldMapCorrectly() {
        // Arrange
        ScooterModelDto dto = new ScooterModelDto(
                1L,
                "Xiaomi mi scoot",
                25,
                40
        );

        // Act
        ScooterModel entity = this.scooterModelMapper.mapToEntity(dto);

        // Assert
        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.title(), entity.getTitle());
        assertEquals(dto.maxSpeed(), entity.getMaxSpeed());
        assertEquals(dto.rangeKm(), entity.getRangeKm());
    }

    @Test
    void updateScooterModelFromDto_shouldIgnoreNulls() {
        // Arrange
        ScooterModel entity = new ScooterModel(
                1L,
                "Xiaomi mi scoot",
                25,
                40
        );

        UpdateScooterModelDto updateDto = new UpdateScooterModelDto(
                null,
                null,
                null
        );

        // Act
        this.scooterModelMapper.updateScooterModelFromDto(updateDto, entity);

        // Assert
        assertEquals(1L, entity.getId());
        assertEquals("Xiaomi mi scoot", entity.getTitle());
        assertEquals(25, entity.getMaxSpeed());
        assertEquals(40, entity.getRangeKm());
    }

    @Test
    void updateScooterModelFromDto_shouldUpdateFields() {
        // Arrange
        ScooterModel entity = new ScooterModel(
                1L,
                "Xiaomi mi scoot",
                25,
                40
        );

        UpdateScooterModelDto updateDto = new UpdateScooterModelDto(
                "Xiaomi mi scoot max",
                null,
                80
        );

        // Act
        this.scooterModelMapper.updateScooterModelFromDto(updateDto, entity);

        // Assert
        assertEquals(1L, entity.getId());
        assertEquals("Xiaomi mi scoot max", entity.getTitle());
        assertEquals(25, entity.getMaxSpeed());
        assertEquals(80, entity.getRangeKm());
    }
}