package ru.noleg.scootrent.service.rental.pause;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.rental.RentalStatus;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.RentalRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalPauserImplTest {

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private RentalPauserImpl rentalPauser;

    @Test
    void pauseRental_shouldUpdateStatusAndSave_whenDataIsValid() {
        // Arrange
        Long userId = 5L;
        Long rentalId = 1L;
        Rental rental = mock(Rental.class);
        when(rental.getRentalStatus()).thenReturn(RentalStatus.ACTIVE);

        when(this.rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        when(this.rentalRepository.isRentalOwnedByUser(rentalId, userId)).thenReturn(true);

        // Act
        this.rentalPauser.pauseRental(rentalId, userId);

        // Assert
        verify(rental).setRentalStatus(RentalStatus.PAUSE);
        verify(rental).setLastPauseTime(any(LocalDateTime.class));
        verify(this.rentalRepository, times(1)).save(rental);
    }


    @Test
    void pauseRental_shouldThrowNotFoundException_whenRentalNotExists() {
        // Arrange
        Long userId = 5L;
        Long rentalId = 1L;

        when(this.rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.rentalPauser.pauseRental(rentalId, userId));
        assertEquals("Rental with id: 1 not found.", ex.getMessage());

        verify(this.rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    void pauseRental_shouldThrowBusinessLogicException_whenUserNotOwnerRental() {
        // Arrange
        Long userId = 5L;
        Long rentalId = 1L;
        Rental rental = mock(Rental.class);

        when(this.rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        when(this.rentalRepository.isRentalOwnedByUser(rentalId, userId)).thenReturn(false);

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.rentalPauser.pauseRental(rentalId, userId));
        assertEquals("The user with id: 5  does not have a rental with id: 1", ex.getMessage());

        verify(this.rentalRepository, never()).save(any(Rental.class));
    }


    @Test
    void pauseRental_shouldThrowException_whenRentalAlreadyPaused() {
        // Arrange
        Long userId = 5L;
        Long rentalId = 1L;
        Rental rental = mock(Rental.class);
        when(rental.getRentalStatus()).thenReturn(RentalStatus.PAUSE);

        when(this.rentalRepository.isRentalOwnedByUser(rentalId, userId)).thenReturn(true);
        when(this.rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.rentalPauser.pauseRental(rentalId, userId));
        assertEquals("Rental is already paused.", ex.getMessage());

        verify(this.rentalRepository, never()).save(any(Rental.class));
    }


    @Test
    void pauseRental_shouldThrowException_whenRentalCompleted() {
        // Arrange
        Long userId = 5L;
        Long rentalId = 1L;
        Rental rental = mock(Rental.class);
        when(rental.getRentalStatus()).thenReturn(RentalStatus.COMPLETED);

        when(this.rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        when(this.rentalRepository.isRentalOwnedByUser(rentalId, userId)).thenReturn(true);

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.rentalPauser.pauseRental(rentalId, userId));
        assertEquals("Rental is already completed.", ex.getMessage());

        verify(this.rentalRepository, never()).save(any(Rental.class));
    }
}