package ru.noleg.scootrent.controller.scooter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.noleg.scootrent.dto.scooter.scootermodel.ScooterModelDto;
import ru.noleg.scootrent.dto.scooter.scootermodel.UpdateScooterModelDto;
import ru.noleg.scootrent.entity.scooter.ScooterModel;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.RepositoryException;
import ru.noleg.scootrent.mapper.ScooterModelMapper;
import ru.noleg.scootrent.service.scooter.ScooterModelService;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScooterModelControllerTest {

    @Mock
    private ScooterModelService scooterModelService;

    @Mock
    private ScooterModelMapper scooterModelMapper;

    @InjectMocks
    private ScooterModelController scooterModelController;

    @Test
    void addScooterModel_shouldReturnCreatedId() {
        // Assert
        Long scooterModelId = 1L;
        ScooterModelDto scooterModelDto = mock(ScooterModelDto.class);
        ScooterModel scooterModel = mock(ScooterModel.class);

        when(this.scooterModelMapper.mapToEntity(scooterModelDto)).thenReturn(scooterModel);
        when(this.scooterModelService.add(scooterModel)).thenReturn(scooterModelId);

        // Act
        ResponseEntity<Long> response = this.scooterModelController.addScooterModel(scooterModelDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(scooterModelId, response.getBody());

        verify(this.scooterModelMapper, times(1)).mapToEntity(scooterModelDto);
        verify(this.scooterModelService, times(1)).add(scooterModel);
    }

    @Test
    void updateScooterModel_shouldUpdateAndReturnDto() {
        // Arrange
        Long scooterModelId = 1L;
        UpdateScooterModelDto updateDto = mock(UpdateScooterModelDto.class);
        ScooterModel scooterModel = mock(ScooterModel.class);
        ScooterModelDto expectedDto = mock(ScooterModelDto.class);

        when(this.scooterModelService.getScooterModel(scooterModelId)).thenReturn(scooterModel);
        doNothing().when(this.scooterModelMapper).updateScooterModelFromDto(updateDto, scooterModel);
        when(this.scooterModelService.add(scooterModel)).thenReturn(scooterModelId);
        when(this.scooterModelMapper.mapToDto(scooterModel)).thenReturn(expectedDto);

        // Act
        ResponseEntity<ScooterModelDto> response = this.scooterModelController.updateScooterModel(scooterModelId, updateDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDto, response.getBody());

        verify(this.scooterModelService, times(1)).getScooterModel(scooterModelId);
        verify(this.scooterModelMapper, times(1)).updateScooterModelFromDto(updateDto, scooterModel);
        verify(this.scooterModelService, times(1)).add(scooterModel);
        verify(this.scooterModelMapper, times(1)).mapToDto(scooterModel);
    }

    @Test
    void updateScooterModel_shouldThrowException_whenScooterModelNotFound() {
        // Arrange
        Long scooterModelId = 100L;
        UpdateScooterModelDto dto = mock(UpdateScooterModelDto.class);

        when(this.scooterModelService.getScooterModel(scooterModelId)).thenThrow(new NotFoundException("Scooter model not found"));

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.scooterModelController.updateScooterModel(scooterModelId, dto)
        );

        assertEquals("Scooter model not found", ex.getMessage());

        verify(this.scooterModelService, times(1)).getScooterModel(scooterModelId);
        verifyNoMoreInteractions(this.scooterModelService);
    }

    @Test
    void deleteScooterModel_shouldCallServiceAndReturnOk() {
        // Arrange
        Long scooterModelId = 1L;
        doNothing().when(this.scooterModelService).delete(scooterModelId);

        // Act
        ResponseEntity<Void> response = this.scooterModelController.deleteScooterModel(scooterModelId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        verify(this.scooterModelService, times(1)).delete(scooterModelId);
    }

    @Test
    void deleteScooterModel_shouldThrowException_whenScooterModelNotFound() {
        // Arrange
        Long scooterModelId = 100L;
        doThrow(new NotFoundException("Scooter model not found")).when(this.scooterModelService).delete(scooterModelId);

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.scooterModelController.deleteScooterModel(scooterModelId)
        );

        assertEquals("Scooter model not found", ex.getMessage());

        verify(this.scooterModelService, times(1)).delete(scooterModelId);
        verifyNoMoreInteractions(this.scooterModelService);
    }

    @Test
    void getAllScooterModels_shouldReturnListOfDtos() {
        // Arrange
        List<ScooterModel> entities = List.of(mock(ScooterModel.class), mock(ScooterModel.class));
        List<ScooterModelDto> dtos = List.of(mock(ScooterModelDto.class), mock(ScooterModelDto.class));

        when(this.scooterModelService.getAllScooterModels()).thenReturn(entities);
        when(this.scooterModelMapper.mapToDtos(entities)).thenReturn(dtos);

        // Act
        ResponseEntity<List<ScooterModelDto>> response = this.scooterModelController.getAllScooterModels();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dtos, response.getBody());

        verify(this.scooterModelService, times(1)).getAllScooterModels();
        verify(this.scooterModelMapper, times(1)).mapToDtos(entities);
    }

    @Test
    void getAllScooterModels_shouldReturnEmptyList() {
        // Arrange
        when(this.scooterModelService.getAllScooterModels()).thenReturn(List.of());
        when(this.scooterModelMapper.mapToDtos(List.of())).thenReturn(List.of());

        // Act
        ResponseEntity<List<ScooterModelDto>> response = this.scooterModelController.getAllScooterModels();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());

        verify(this.scooterModelService, times(1)).getAllScooterModels();
        verify(this.scooterModelMapper, times(1)).mapToDtos(List.of());
    }

    @Test
    void getAllScooterModels_shouldThrowException_whenDbError() {
        // Arrange
        when(this.scooterModelService.getAllScooterModels()).thenThrow(new RepositoryException("Database error"));

        // Act | Assert
        RepositoryException ex = assertThrows(RepositoryException.class,
                () -> this.scooterModelController.getAllScooterModels()
        );

        assertEquals("Database error", ex.getMessage());

        verify(this.scooterModelService, times(1)).getAllScooterModels();
    }
}

