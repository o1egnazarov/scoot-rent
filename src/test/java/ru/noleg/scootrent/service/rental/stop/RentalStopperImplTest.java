package ru.noleg.scootrent.service.rental.stop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.location.LocationType;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.rental.RentalStatus;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.repository.LocationRepository;
import ru.noleg.scootrent.repository.RentalRepository;
import ru.noleg.scootrent.service.rental.billing.BillingService;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalStopperImplTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private BillingService billingService;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private RentalStopperImpl rentalStopper;

    @Test
    void stopRental_shouldCompleteRental_whenAllValid() {
        // Arrange
        Long rentalId = 1L;
        Long endPointId = 2L;
        RentalStatus rentalStatus = RentalStatus.ACTIVE;
        LocationType locationType = LocationType.RENTAL_POINT;

        Rental rental = mock(Rental.class);
        LocationNode location = mock(LocationNode.class);
        Tariff tariff = mock(Tariff.class);
        User user = mock(User.class);
        Scooter scooter = mock(Scooter.class);

        when(rental.getRentalStatus()).thenReturn(rentalStatus);
        when(rental.getStartTime()).thenReturn((LocalDateTime.now().minusMinutes(60)));
        when(rental.getDurationInPause()).thenReturn(Duration.ofMinutes(10));
        when(rental.getTariff()).thenReturn(tariff);
        when(rental.getUser()).thenReturn(user);
        when(rental.getScooter()).thenReturn(scooter);

        when(location.getLocationType()).thenReturn(locationType);

        when(this.rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        when(this.locationRepository.findById(endPointId)).thenReturn(Optional.of(location));
        when(this.billingService.calculateRentalCost(any(), any(), any())).thenReturn(new BigDecimal("42.00"));

        // Act
        this.rentalStopper.stopRental(rentalId, endPointId);

        // Assert
        verify(this.rentalRepository, times(1)).findById(rentalId);
        verify(this.locationRepository, times(1)).findById(endPointId);
        verify(rental, times(1))
                .stopRental(any(LocationNode.class), any(BigDecimal.class), any(Duration.class));
        verify(this.rentalRepository, times(1)).save(rental);
    }

    @Test
    void stopRental_shouldThrowNotFoundException_whenRentalNotFound() {
        // Arrange
        Long rentalId = 1L;

        when(this.rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.rentalStopper.stopRental(rentalId, 2L));
        assertEquals("Rental with id: 1 not found.", ex.getMessage());

        verify(this.rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    void stopRental_shouldThrowBusinessLogicException_whenRentalAlreadyCompleted() {
        // Arrange
        Long rentalId = 1L;
        RentalStatus rentalStatus = RentalStatus.COMPLETED;

        Rental rental = mock(Rental.class);
        when(rental.getRentalStatus()).thenReturn(rentalStatus);

        when(this.rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.rentalStopper.stopRental(rentalId, 2L));
        assertEquals("Rental is already completed.", ex.getMessage());

        verify(this.rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    void stopRental_shouldThrowNotFoundException_whenLocationNotFound() {
        // Arrange
        Long rentalId = 1L;
        Long endPointId = 2L;
        RentalStatus rentalStatus = RentalStatus.ACTIVE;

        Rental rental = mock(Rental.class);
        when(rental.getRentalStatus()).thenReturn(rentalStatus);

        when(this.rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        when(this.locationRepository.findById(endPointId)).thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.rentalStopper.stopRental(rentalId, endPointId));
        assertEquals("Rental point with id: 2 not found.", ex.getMessage());

        verify(this.rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    void stopRental_shouldThrowBusinessLogicException_whenLocationIsNotRentalPoint() {
        // Arrange
        Long rentalId = 1L;
        Long endPointId = 2L;
        RentalStatus rentalStatus = RentalStatus.ACTIVE;
        LocationType locationType = LocationType.CITY;

        Rental rental = mock(Rental.class);
        LocationNode location = mock(LocationNode.class);

        when(rental.getRentalStatus()).thenReturn(rentalStatus);
        when(location.getLocationType()).thenReturn(locationType);

        when(this.rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        when(this.locationRepository.findById(endPointId)).thenReturn(Optional.of(location));

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.rentalStopper.stopRental(rentalId, endPointId));
        assertEquals("Location is not a rental point.", ex.getMessage());

        verify(this.rentalRepository, never()).save(any(Rental.class));
    }


    @Test
    void stopRental_shouldThrowServiceException_whenBillingServiceFails() {
        // Arrange
        Long rentalId = 1L;
        Long endPointId = 2L;
        RentalStatus rentalStatus = RentalStatus.ACTIVE;
        LocationType locationType = LocationType.RENTAL_POINT;

        Rental rental = mock(Rental.class);
        LocationNode location = mock(LocationNode.class);
        Tariff tariff = mock(Tariff.class);
        User user = mock(User.class);

        when(rental.getRentalStatus()).thenReturn(rentalStatus);
        when(rental.getStartTime()).thenReturn(LocalDateTime.now().minusMinutes(60));
        when(rental.getDurationInPause()).thenReturn(Duration.ofMinutes(10));
        when(rental.getTariff()).thenReturn(tariff);
        when(rental.getUser()).thenReturn(user);
        when(location.getLocationType()).thenReturn(locationType);

        when(this.rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        when(this.locationRepository.findById(endPointId)).thenReturn(Optional.of(location));
        when(this.billingService.calculateRentalCost(any(User.class), any(Tariff.class), any(Duration.class)))
                .thenThrow(new RuntimeException("Billing failure"));

        // Act | Assert
        ServiceException ex = assertThrows(ServiceException.class,
                () -> this.rentalStopper.stopRental(rentalId, endPointId));
        assertEquals("Error in BillingService by rental with id 1", ex.getMessage());

        verify(this.rentalRepository, never()).save(any(Rental.class));
    }
}