package ru.noleg.scootrent.controller.tariff;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.noleg.scootrent.dto.tariff.TariffDto;
import ru.noleg.scootrent.dto.tariff.UpdateTariffDto;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.RepositoryException;
import ru.noleg.scootrent.mapper.TariffMapper;
import ru.noleg.scootrent.service.tariff.TariffService;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TariffControllerTest {
    @Mock
    private TariffService tariffService;

    @Mock
    private TariffMapper tariffMapper;

    @InjectMocks
    private TariffController tariffController;

    @Test
    void addTariff_shouldReturnCreatedId() {
        // Arrange
        Long tariffId = 1L;
        TariffDto tariffDto = mock(TariffDto.class);
        Tariff tariff = mock(Tariff.class);

        when(this.tariffMapper.mapToEntity(tariffDto)).thenReturn(tariff);
        when(this.tariffService.createTariff(tariff)).thenReturn(tariffId);

        // Act
        ResponseEntity<Long> response = this.tariffController.addTariff(tariffDto);

        // Arrange
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(tariffId, response.getBody());

        verify(this.tariffMapper, times(1)).mapToEntity(tariffDto);
        verify(this.tariffService, times(1)).createTariff(tariff);
    }

    @Test
    void updateUpdateTariff_shouldUpdateAndReturnDto() {
        // Arrange
        UpdateTariffDto updateTariffDto = mock(UpdateTariffDto.class);
        Tariff tariff = mock(Tariff.class);
        TariffDto tariffDto = mock(TariffDto.class);

        when(this.tariffService.getTariff(1L)).thenReturn(tariff);
        doNothing().when(this.tariffMapper).updateTariffFromDto(updateTariffDto, tariff);
        when(this.tariffService.createTariff(tariff)).thenReturn(1L);
        when(this.tariffMapper.mapToDto(tariff)).thenReturn(tariffDto);

        // Act
        ResponseEntity<TariffDto> response = this.tariffController.updateTariff(1L, updateTariffDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tariffDto, response.getBody());

        verify(this.tariffService, times(1)).getTariff(1L);
        verify(this.tariffMapper, times(1)).updateTariffFromDto(updateTariffDto, tariff);
        verify(this.tariffService, times(1)).createTariff(tariff);
        verify(this.tariffMapper, times(1)).mapToDto(tariff);
    }

    @Test
    void updateTariff_shouldThrowException_whenTariffNotFound() {
        // Arrange
        Long tariffId = 100L;
        UpdateTariffDto dto = mock(UpdateTariffDto.class);

        when(this.tariffService.getTariff(tariffId)).thenThrow(new NotFoundException("Tariff not found"));

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.tariffController.updateTariff(tariffId, dto)
        );

        assertEquals("Tariff not found", ex.getMessage());

        verify(this.tariffService, times(1)).getTariff(tariffId);
        verifyNoMoreInteractions(this.tariffService);
    }

    @Test
    void deactivateTariff_shouldCallServiceAndReturnOk() {
        // Arrange
        Long tariffId = 1L;
        doNothing().when(this.tariffService).deactivateTariff(tariffId);

        // Act
        ResponseEntity<Void> response = this.tariffController.deactivateTariff(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(this.tariffService, times(1)).deactivateTariff(tariffId);
    }

    @Test
    void deactivateTariff_shouldThrowException_whenTariffNotFound() {
        // Arrange
        Long tariffId = 100L;
        doThrow(new NotFoundException("Tariff not found")).when(this.tariffService).deactivateTariff(tariffId);

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.tariffController.deactivateTariff(tariffId)
        );

        assertEquals("Tariff not found", ex.getMessage());

        verify(this.tariffService, times(1)).deactivateTariff(tariffId);
        verifyNoMoreInteractions(this.tariffService);
    }

    @Test
    void deactivateTariff_shouldThrowException_whenTariffAlreadyDeactivate() {
        // Arrange
        Long tariffId = 100L;
        doThrow(new BusinessLogicException("Tariff already disable")).when(this.tariffService).deactivateTariff(tariffId);

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.tariffController.deactivateTariff(tariffId)
        );

        assertEquals("Tariff already disable", ex.getMessage());

        verify(this.tariffService, times(1)).deactivateTariff(tariffId);
        verifyNoMoreInteractions(this.tariffService);
    }

    @Test
    void activateTariff_shouldCallServiceAndReturnOk() {
        // Arrange
        Long tariffId = 1L;
        doNothing().when(this.tariffService).activateTariff(tariffId);

        // Act
        ResponseEntity<Void> response = this.tariffController.activateTariff(tariffId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(this.tariffService, times(1)).activateTariff(tariffId);
    }

    @Test
    void activateTariff_shouldThrowException_whenTariffNotFound() {
        // Arrange
        Long tariffId = 100L;
        doThrow(new NotFoundException("Tariff not found")).when(this.tariffService).activateTariff(tariffId);

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.tariffController.activateTariff(tariffId)
        );

        assertEquals("Tariff not found", ex.getMessage());

        verify(this.tariffService, times(1)).activateTariff(tariffId);
        verifyNoMoreInteractions(this.tariffService);
    }

    @Test
    void activateTariff_shouldThrowException_whenTariffAlreadyActivate() {
        // Arrange
        Long tariffId = 100L;
        doThrow(new BusinessLogicException("Tariff already active")).when(this.tariffService).activateTariff(tariffId);

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.tariffController.activateTariff(tariffId)
        );

        assertEquals("Tariff already active", ex.getMessage());

        verify(this.tariffService, times(1)).activateTariff(tariffId);
        verifyNoMoreInteractions(this.tariffService);
    }

    @Test
    void getActiveTariffs_shouldReturnActiveTariffs() {
        // Arrange
        Tariff tariff1 = mock(Tariff.class);
        Tariff tariff2 = mock(Tariff.class);

        TariffDto tariffDto1 = mock(TariffDto.class);
        TariffDto tariffDto2 = mock(TariffDto.class);

        List<Tariff> tariffs = List.of(tariff1, tariff2);
        List<TariffDto> dtos = List.of(tariffDto1, tariffDto2);

        when(this.tariffService.getActiveTariffs()).thenReturn(tariffs);
        when(this.tariffMapper.mapToDtos(tariffs)).thenReturn(dtos);

        // Act
        ResponseEntity<List<TariffDto>> response = this.tariffController.getActiveTariffs();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(tariffDto1, tariffDto2), response.getBody());

        verify(this.tariffService, times(1)).getActiveTariffs();
        verify(this.tariffMapper, times(1)).mapToDtos(tariffs);
    }

    @Test
    void getActiveTariffs_shouldReturnEmptyList() {
        // Arrange
        List<Tariff> tariffs = List.of();
        List<TariffDto> dtos = List.of();

        when(this.tariffService.getActiveTariffs()).thenReturn(tariffs);
        when(this.tariffMapper.mapToDtos(tariffs)).thenReturn(dtos);

        // Act
        ResponseEntity<List<TariffDto>> response = this.tariffController.getActiveTariffs();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());

        verify(this.tariffService, times(1)).getActiveTariffs();
        verify(this.tariffMapper, times(1)).mapToDtos(tariffs);
    }

    @Test
    void getActiveTariffs_shouldThrowException_whenDbError() {
        // Arrange
        when(this.tariffService.getActiveTariffs()).thenThrow(new RepositoryException("Database error"));

        // Act | Assert
        RepositoryException ex = assertThrows(RepositoryException.class,
                () -> this.tariffController.getActiveTariffs()
        );

        assertEquals("Database error", ex.getMessage());

        verify(this.tariffService, times(1)).getActiveTariffs();
        verifyNoMoreInteractions(this.tariffService);
    }

    @Test
    void getTariffs_shouldReturnTariffs() {
        // Arrange
        Tariff tariff1 = mock(Tariff.class);
        Tariff tariff2 = mock(Tariff.class);

        TariffDto tariffDto1 = mock(TariffDto.class);
        TariffDto tariffDto2 = mock(TariffDto.class);

        List<Tariff> tariffs = List.of(tariff1, tariff2);
        List<TariffDto> dtos = List.of(tariffDto1, tariffDto2);

        when(this.tariffService.getAllTariffs()).thenReturn(tariffs);
        when(this.tariffMapper.mapToDtos(tariffs)).thenReturn(dtos);

        // Act
        ResponseEntity<List<TariffDto>> response = this.tariffController.getTariffs();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(tariffDto1, tariffDto2), response.getBody());

        verify(this.tariffService, times(1)).getAllTariffs();
        verify(this.tariffMapper, times(1)).mapToDtos(tariffs);
    }

    @Test
    void getTariffs_shouldReturnEmptyList() {
        // Arrange
        List<Tariff> tariffs = List.of();
        List<TariffDto> dtos = List.of();

        when(this.tariffService.getAllTariffs()).thenReturn(tariffs);
        when(this.tariffMapper.mapToDtos(tariffs)).thenReturn(dtos);

        // Act
        ResponseEntity<List<TariffDto>> response = this.tariffController.getTariffs();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());

        verify(this.tariffService, times(1)).getAllTariffs();
        verify(this.tariffMapper, times(1)).mapToDtos(tariffs);
    }


    @Test
    void getAllTariffs_shouldThrowException_whenDbError() {
        // Arrange
        when(this.tariffService.getAllTariffs()).thenThrow(new RepositoryException("Database error"));

        // Act | Assert
        RepositoryException ex = assertThrows(RepositoryException.class,
                () -> this.tariffController.getTariffs()
        );

        assertEquals("Database error", ex.getMessage());

        verify(this.tariffService, times(1)).getAllTariffs();
        verifyNoMoreInteractions(this.tariffService);
    }
}