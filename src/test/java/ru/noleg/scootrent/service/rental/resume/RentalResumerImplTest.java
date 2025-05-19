package ru.noleg.scootrent.service.rental.resume;

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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalResumerImplTest {

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private RentalResumerImpl rentalResumer;


    @Test
    void resumeRental_shouldUpdateStatusAndSave_whenDataIsValid() {
        // Arrange
        Long rentalId = 1L;
        Rental rental = mock(Rental.class);
        LocalDateTime lastPauseTime = LocalDateTime.now().minusMinutes(10);

        when(rental.getRentalStatus()).thenReturn(RentalStatus.PAUSE);
        when(rental.getLastPauseTime()).thenReturn(lastPauseTime);

        when(this.rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        // Act
        this.rentalResumer.resumeRental(rentalId);

        // Assert
        verify(rental).addPause(any(Duration.class));
        verify(rental).setLastPauseTime(null);
        verify(rental).setRentalStatus(RentalStatus.ACTIVE);
        verify(this.rentalRepository).save(rental);
    }

    @Test
    void resumeRental_shouldThrowNotFoundException_whenRentalNotExists() {
        // Arrange
        Long rentalId = 1L;

        when(this.rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.rentalResumer.resumeRental(rentalId));
        assertEquals("Rental with id: 1 not found.", ex.getMessage());

        verify(this.rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    void resumeRental_shouldThrowException_whenRentalStatusIsNotPause() {
        // Arrange
        Long rentalId = 1L;
        Rental rental = mock(Rental.class);
        when(rental.getRentalStatus()).thenReturn(RentalStatus.ACTIVE);

        when(this.rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.rentalResumer.resumeRental(rentalId));
        assertEquals("Rental is already used.", ex.getMessage());

        verify(this.rentalRepository, never()).save(any(Rental.class));
    }
}