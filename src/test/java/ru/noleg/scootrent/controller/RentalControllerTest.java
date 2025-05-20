package ru.noleg.scootrent.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.noleg.scootrent.controller.rental.RentalController;
import ru.noleg.scootrent.dto.rental.ShortRentalDto;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.mapper.RentalMapper;
import ru.noleg.scootrent.service.rental.RentalService;
import ru.noleg.scootrent.service.user.UserDetailsImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalControllerTest {

    @Mock
    private RentalService rentalService;

    @Mock
    private RentalMapper rentalMapper;

    @InjectMocks
    private RentalController rentalController;

    @Test
    void startRental_shouldCallServiceAndReturnRentalId() {
        // Arrange
        Long userId = 1L;
        Long scooterId = 2L;
        Long pointId = 3L;
        Long rentalId = 4L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);
        when(this.rentalService.startRental(userId, scooterId, pointId, billingMode)).thenReturn(rentalId);

        // Act
        ResponseEntity<Long> response = this.rentalController.startRental(userDetails, scooterId, pointId, billingMode);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rentalId, response.getBody());
        verify(this.rentalService, times(1)).startRental(userId, scooterId, pointId, billingMode);
    }

    @Test
    void startRental_shouldThrownException_whenServiceFails() {
        // Arrange
        Long userId = 1L;
        Long scooterId = 2L;
        Long pointId = 3L;
        BillingMode billingMode = BillingMode.PER_MINUTE;

        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);

        when(this.rentalService.startRental(userId, scooterId, pointId, billingMode))
                .thenThrow(new BusinessLogicException("Rental already active"));

        // Act | Assert
        BusinessLogicException exception = assertThrows(BusinessLogicException.class, () ->
                this.rentalController.startRental(userDetails, scooterId, pointId, billingMode)
        );

        assertEquals("Rental already active", exception.getMessage());

        verify(this.rentalService, times(1))
                .startRental(userId, scooterId, pointId, billingMode);
    }

    @Test
    void pauseRental_shouldCallServiceAndReturnOk() {
        // Arrange
        Long rentalId = 1L;
        Long userId = 1L;
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);

        // Act
        ResponseEntity<Void> response = this.rentalController.pauseRental(userDetails, rentalId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(this.rentalService, times(1)).pauseRental(rentalId, userId);
    }

    @Test
    void pauseRental_shouldThrownException_whenRentalNotFound() {
        // Arrange
        Long rentalId = 100L;
        Long userId = 1L;
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);

        doThrow(new NotFoundException("Rental not found"))
                .when(this.rentalService).pauseRental(rentalId, userId);

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () ->
                this.rentalController.pauseRental(userDetails, rentalId)
        );

        assertEquals("Rental not found", ex.getMessage());
        verify(rentalService, times(1)).pauseRental(rentalId, userId);
    }


    @Test
    void resumeRental_shouldCallServiceAndReturnOk() {
        // Arrange
        Long rentalId = 2L;
        Long userId = 1L;
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);

        // Act
        ResponseEntity<Void> response = this.rentalController.resumeRental(userDetails, rentalId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(this.rentalService, times(1)).resumeRental(rentalId, userId);
    }

    @Test
    void resumeRental_shouldThrownException_whenAlreadyStopped() {
        // Arrange
        Long rentalId = 7L;
        Long userId = 1L;
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);

        doThrow(new BusinessLogicException("Rental is already stopped"))
                .when(rentalService).resumeRental(rentalId, userId);

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () ->
                rentalController.resumeRental(userDetails, rentalId)
        );

        assertEquals("Rental is already stopped", ex.getMessage());
        verify(this.rentalService, times(1)).resumeRental(rentalId, userId);
    }

    @Test
    void endRental_shouldCallServiceAndReturnOk() {
        // Arrange
        Long rentalId = 3L;
        Long endPointId = 5L;
        Long userId = 1L;
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);

        // Act
        ResponseEntity<Void> response = this.rentalController.endRental(userDetails, rentalId, endPointId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(this.rentalService, times(1)).stopRental(rentalId, endPointId, userId);
    }

    @Test
    void endRental_shouldThrownException_whenInvalidPoint() {
        // Arrange
        Long rentalId = 1L;
        Long endPointId = 100L;
        Long userId = 1L;
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);

        doThrow(new BusinessLogicException("Invalid end point"))
                .when(this.rentalService).stopRental(rentalId, endPointId, userId);

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () ->
                this.rentalController.endRental(userDetails, rentalId, endPointId)
        );

        assertEquals("Invalid end point", ex.getMessage());
        verify(this.rentalService, times(1)).stopRental(rentalId, endPointId, userId);
    }

    @Test
    void getRentals_shouldReturnRentals() {
        // Arrange
        List<Rental> rentals = List.of(mock(Rental.class));
        List<ShortRentalDto> dtos = List.of(mock(ShortRentalDto.class));

        when(this.rentalService.getRentals()).thenReturn(rentals);
        when(this.rentalMapper.mapToDtos(rentals)).thenReturn(dtos);

        // Act
        ResponseEntity<List<ShortRentalDto>> response = this.rentalController.getRentals();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dtos, response.getBody());

        verify(this.rentalService, times(1)).getRentals();
        verify(this.rentalMapper, times(1)).mapToDtos(rentals);
    }

    @Test
    void getRentals_shouldReturnEmptyList_whenNoRentals() {
        // Arrange
        when(this.rentalService.getRentals()).thenReturn(List.of());
        when(this.rentalMapper.mapToDtos(List.of())).thenReturn(List.of());

        // Act
        ResponseEntity<List<ShortRentalDto>> response = this.rentalController.getRentals();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(this.rentalService, times(1)).getRentals();
        verify(this.rentalMapper, times(1)).mapToDtos(List.of());
    }
}
