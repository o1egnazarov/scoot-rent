package ru.noleg.scootrent.controller.rolebased;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.noleg.scootrent.dto.rental.ScooterRentalHistoryDto;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.mapper.RentalHistoryMapper;
import ru.noleg.scootrent.service.rental.RentalService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModeratorControllerTest {

    @Mock
    private RentalService rentalService;

    @Mock
    private RentalHistoryMapper rentalHistoryMapper;

    @InjectMocks
    private ModeratorController moderatorController;

    @Test
    void getRentalHistory_shouldReturnRentalHistoryList() {
        // Arrange
        Long scooterId = 1L;

        Rental rental1 = mock(Rental.class);
        Rental rental2 = mock(Rental.class);
        List<Rental> rentals = List.of(rental1, rental2);

        ScooterRentalHistoryDto dto1 = mock(ScooterRentalHistoryDto.class);
        ScooterRentalHistoryDto dto2 = mock(ScooterRentalHistoryDto.class);
        List<ScooterRentalHistoryDto> expectedDtos = List.of(dto1, dto2);

        when(this.rentalService.getRentalHistoryForScooter(scooterId)).thenReturn(rentals);
        when(this.rentalHistoryMapper.mapToScooterRentalDtos(rentals)).thenReturn(expectedDtos);

        // Act
        ResponseEntity<List<ScooterRentalHistoryDto>> response = this.moderatorController.getRentalHistory(scooterId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDtos, response.getBody());

        verify(this.rentalService, times(1)).getRentalHistoryForScooter(scooterId);
        verify(this.rentalHistoryMapper, times(1)).mapToScooterRentalDtos(rentals);
    }

    @Test
    void getRentalHistory_shouldReturnEmptyListIfNoRentals() {
        // Arrange
        Long scooterId = 2L;
        when(this.rentalService.getRentalHistoryForScooter(scooterId)).thenReturn(List.of());
        when(this.rentalHistoryMapper.mapToScooterRentalDtos(List.of())).thenReturn(List.of());

        // Act
        ResponseEntity<List<ScooterRentalHistoryDto>> response = this.moderatorController.getRentalHistory(scooterId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(this.rentalService, times(1)).getRentalHistoryForScooter(scooterId);
        verify(this.rentalHistoryMapper, times(1)).mapToScooterRentalDtos(List.of());
    }

    @Test
    void getRentalHistory_shouldThrowException_whenServiceFails() {
        // Arrange
        Long scooterId = 3L;
        when(rentalService.getRentalHistoryForScooter(scooterId))
                .thenThrow(new NotFoundException("Scooter not found"));

        // Act | Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> moderatorController.getRentalHistory(scooterId)
        );

        assertEquals("Scooter not found", exception.getMessage());
        verify(rentalService, times(1)).getRentalHistoryForScooter(scooterId);
        verifyNoInteractions(rentalHistoryMapper);
    }
}