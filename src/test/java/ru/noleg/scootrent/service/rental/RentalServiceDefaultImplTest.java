package ru.noleg.scootrent.service.rental;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.repository.RentalRepository;
import ru.noleg.scootrent.service.rental.pause.RentalPauser;
import ru.noleg.scootrent.service.rental.resume.RentalResumer;
import ru.noleg.scootrent.service.rental.start.RentalStarter;
import ru.noleg.scootrent.service.rental.stop.RentalStopper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceDefaultImplTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private RentalStarter rentalStarter;

    @Mock
    private RentalPauser rentalPauser;

    @Mock
    private RentalResumer rentalResumer;

    @Mock
    private RentalStopper rentalStopper;

    @InjectMocks
    private RentalServiceDefaultImpl rentalService;

    @Test
    void startRental_shouldDelegateToRentalStarter_andReturnId() {
        // Arrange
        Long userId = 1L;
        Long scooterId = 2L;
        Long rentalPointId = 3L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        when(this.rentalStarter.startRental(userId, scooterId, rentalPointId, billingMode)).thenReturn(4L);

        // Act
        Long result = this.rentalService.startRental(userId, scooterId, rentalPointId, billingMode);

        // Assert
        assertEquals(4L, result);

        verify(this.rentalStarter).startRental(userId, scooterId, rentalPointId, billingMode);
    }

    @Test
    void pauseRental_shouldDelegateToRentalPauser() {
        // Arrange
        Long rentalId = 1L;

        // Act
        this.rentalService.pauseRental(rentalId);

        // Assert
        verify(this.rentalPauser).pauseRental(rentalId);
    }

    @Test
    void resumeRental_shouldDelegateToRentalResumer() {
        // Arrange
        Long rentalId = 1L;

        // Act
        this.rentalService.resumeRental(rentalId);

        // Assert
        verify(this.rentalResumer).resumeRental(rentalId);
    }

    @Test
    void stopRental_shouldDelegateToRentalStopper() {
        // Arrange
        Long rentalId = 1L;
        Long endPointId = 5L;

        // Act
        this.rentalService.stopRental(rentalId, endPointId);

        // Assert
        verify(this.rentalStopper).stopRental(rentalId, endPointId);
    }

    @Test
    void getRentals_shouldReturnAllRentals() {
        // Arrange
        List<Rental> rentals = List.of(mock(Rental.class), mock(Rental.class));
        when(this.rentalRepository.findAllRentals()).thenReturn(rentals);

        // Act
        List<Rental> result = this.rentalService.getRentals();

        // Assert
        assertEquals(rentals, result);
        verify(this.rentalRepository).findAllRentals();
    }

    @Test
    void getRentalHistoryForScooter_shouldReturnRentals() {
        // Arrange
        Long scooterId = 1L;
        List<Rental> rentals = List.of(mock(Rental.class));
        when(this.rentalRepository.findRentalForScooter(scooterId)).thenReturn(rentals);

        // Act
        List<Rental> result = this.rentalService.getRentalHistoryForScooter(scooterId);

        // Assert
        assertEquals(rentals, result);
        verify(this.rentalRepository).findRentalForScooter(scooterId);
    }

    @Test
    void getRentalHistoryForUser_shouldReturnRentals() {
        // Arrange
        Long userId = 1L;
        List<Rental> rentals = List.of(mock(Rental.class));
        when(this.rentalRepository.findRentalsForUser(userId)).thenReturn(rentals);

        // Act
        List<Rental> result = this.rentalService.getRentalHistoryForUser(userId);

        // Assert
        assertEquals(rentals, result);
        verify(this.rentalRepository).findRentalsForUser(userId);
    }
}