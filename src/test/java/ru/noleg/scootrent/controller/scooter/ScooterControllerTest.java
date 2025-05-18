package ru.noleg.scootrent.controller.scooter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.noleg.scootrent.dto.scooter.ScooterDto;
import ru.noleg.scootrent.dto.scooter.UpdateScooterDto;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.RepositoryException;
import ru.noleg.scootrent.mapper.ScooterMapper;
import ru.noleg.scootrent.service.scooter.ScooterService;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScooterControllerTest {

    @Mock
    private ScooterService scooterService;

    @Mock
    private ScooterMapper scooterMapper;

    @InjectMocks
    private ScooterController scooterController;

    @Test
    void addScooter_shouldReturnCreatedId() {
        // Arrange
        Long scooterId = 1L;
        ScooterDto scooterDto = mock(ScooterDto.class);
        Scooter scooter = mock(Scooter.class);

        when(scooterDto.numberPlate()).thenReturn("T017PC");

        when(this.scooterMapper.mapToEntity(scooterDto)).thenReturn(scooter);
        when(this.scooterService.add(scooter)).thenReturn(scooterId);

        // Act
        ResponseEntity<Long> response = this.scooterController.addScooter(scooterDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(scooterId, response.getBody());

        verify(this.scooterMapper, times(1)).mapToEntity(scooterDto);
        verify(this.scooterService, times(1)).add(scooter);
    }

    @Test
    void updateScooter_shouldUpdateAndReturnDto() {
        // Arrange
        Long scooterId = 1L;
        UpdateScooterDto updateDto = mock(UpdateScooterDto.class);
        Scooter scooter = mock(Scooter.class);
        ScooterDto expectedDto = mock(ScooterDto.class);

        when(this.scooterService.getScooter(scooterId)).thenReturn(scooter);
        doNothing().when(this.scooterMapper).updateScooterFromDto(updateDto, scooter);
        when(this.scooterService.add(scooter)).thenReturn(scooterId);
        when(this.scooterMapper.mapToDto(scooter)).thenReturn(expectedDto);

        // Act
        ResponseEntity<ScooterDto> response = this.scooterController.updateScooter(scooterId, updateDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDto, response.getBody());

        verify(this.scooterService, times(1)).getScooter(scooterId);
        verify(this.scooterMapper, times(1)).updateScooterFromDto(updateDto, scooter);
        verify(this.scooterService, times(1)).add(scooter);
        verify(this.scooterMapper, times(1)).mapToDto(scooter);
    }

    @Test
    void updateScooter_shouldThrowException_whenScooterNotFound() {
        // Arrange
        Long scooterId = 100L;
        UpdateScooterDto dto = mock(UpdateScooterDto.class);
        when(this.scooterService.getScooter(scooterId)).thenThrow(new NotFoundException("Scooter not found"));

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.scooterController.updateScooter(scooterId, dto)
        );

        assertEquals("Scooter not found", ex.getMessage());

        verify(this.scooterService, times(1)).getScooter(scooterId);
        verifyNoMoreInteractions(this.scooterService);
    }

    @Test
    void deleteScooter_shouldCallServiceAndReturnOk() {
        // Arrange
        Long scooterId = 1L;
        doNothing().when(this.scooterService).delete(scooterId);

        // Act
        ResponseEntity<Void> response = this.scooterController.deleteScooter(scooterId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        verify(this.scooterService, times(1)).delete(scooterId);
    }

    @Test
    void deleteScooter_shouldThrowException_whenScooterNotFound() {
        // Arrange
        Long scooterId = 100L;
        doThrow(new NotFoundException("Scooter not found")).when(this.scooterService).delete(scooterId);

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.scooterController.deleteScooter(scooterId)
        );

        assertEquals("Scooter not found", ex.getMessage());

        verify(this.scooterService, times(1)).delete(scooterId);
    }

    @Test
    void getAllScooters_shouldReturnListOfDtos() {
        // Arrange
        List<Scooter> entities = List.of(mock(Scooter.class), mock(Scooter.class));
        List<ScooterDto> dtos = List.of(mock(ScooterDto.class), mock(ScooterDto.class));

        when(this.scooterService.getAllScooters()).thenReturn(entities);
        when(this.scooterMapper.mapToDtos(entities)).thenReturn(dtos);

        // Act
        ResponseEntity<List<ScooterDto>> response = this.scooterController.getAllScooters();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dtos, response.getBody());

        verify(this.scooterService, times(1)).getAllScooters();
        verify(this.scooterMapper, times(1)).mapToDtos(entities);
    }

    @Test
    void getAllScooters_shouldReturnEmptyList() {
        // Arrange
        when(this.scooterService.getAllScooters()).thenReturn(List.of());
        when(this.scooterMapper.mapToDtos(List.of())).thenReturn(List.of());

        // Act
        ResponseEntity<List<ScooterDto>> response = this.scooterController.getAllScooters();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());

        verify(this.scooterService, times(1)).getAllScooters();
        verify(this.scooterMapper, times(1)).mapToDtos(List.of());
    }

    @Test
    void getAllScooters_shouldThrowException_whenDbError() {
        // Arrange
        when(this.scooterService.getAllScooters()).thenThrow(new RepositoryException("Database error"));

        // Act | Assert
        RepositoryException ex = assertThrows(RepositoryException.class,
                () -> this.scooterController.getAllScooters()
        );

        assertEquals("Database error", ex.getMessage());

        verify(this.scooterService, times(1)).getAllScooters();
    }
}