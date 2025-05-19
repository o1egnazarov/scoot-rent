package ru.noleg.scootrent.service.rental.start;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.location.LocationType;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.entity.scooter.ScooterStatus;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.exception.UserNotFoundException;
import ru.noleg.scootrent.repository.LocationRepository;
import ru.noleg.scootrent.repository.RentalRepository;
import ru.noleg.scootrent.repository.ScooterRepository;
import ru.noleg.scootrent.repository.UserRepository;
import ru.noleg.scootrent.service.rental.tariffselect.TariffSelectionService;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalStarterImplTest {
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ScooterRepository scooterRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private TariffSelectionService tariffSelectionService;

    @InjectMocks
    private RentalStarterImpl rentalStarter;

    @Test
    void startRental_shouldCreateRental_whenDataIsValid() {
        // Arrange
        Long userId = 1L;
        Long scooterId = 2L;
        Long startPointId = 3L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        User user = mock(User.class);
        Scooter scooter = mock(Scooter.class);
        LocationNode rentalPoint = mock(LocationNode.class);
        Tariff tariff = mock(Tariff.class);

        when(scooter.getStatus()).thenReturn(ScooterStatus.FREE);
        when(rentalPoint.getLocationType()).thenReturn(LocationType.RENTAL_POINT);
        when(rentalPoint.getScooters()).thenReturn(Set.of(scooter));

        when(this.scooterRepository.findById(scooterId)).thenReturn(Optional.of(scooter));
        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(this.locationRepository.findById(startPointId)).thenReturn(Optional.of(rentalPoint));
        when(this.rentalRepository.isActiveRentalByUserId(userId)).thenReturn(false);
        when(this.tariffSelectionService.selectTariffForUser(userId, billingMode)).thenReturn(tariff);
        when(this.rentalRepository.save(any(Rental.class))).thenAnswer(invocation -> {
            Rental saved = invocation.getArgument(0);
            ReflectionTestUtils.setField(saved, "id", 100L);
            return saved;
        });

        // Act
        Long rentalId = this.rentalStarter.startRental(userId, scooterId, startPointId, billingMode);

        // Assert
        assertEquals(100L, rentalId);
        verify(this.scooterRepository, times(1)).findById(scooterId);
        verify(this.userRepository, times(1)).findById(userId);
        verify(this.locationRepository, times(1)).findById(startPointId);
        verify(this.rentalRepository, times(1)).isActiveRentalByUserId(userId);
        verify(this.tariffSelectionService, times(1)).selectTariffForUser(userId, billingMode);
        verify(this.rentalRepository, times(1)).save(any(Rental.class));
    }

    @Test
    void startRental_shouldThrowNotFoundException_whenScooterNotFound() {
        // Arrange
        Long userId = 1L;
        Long scooterId = 2L;
        Long rentalPointId = 3L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        when(this.scooterRepository.findById(scooterId)).thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.rentalStarter.startRental(userId, scooterId, rentalPointId, billingMode)
        );
        assertEquals("Scooter with id: 2 not found.", ex.getMessage());

        verify(this.scooterRepository, times(1)).findById(scooterId);
        verify(this.rentalRepository, never()).save(any());
    }

    @Test
    void startRental_shouldThrowBusinessLogicException_whenScooterNotFree() {
        // Arrange
        Long userId = 1L;
        Long scooterId = 2L;
        Long rentalPointId = 3L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        Scooter scooter = mock(Scooter.class);
        when(scooter.getStatus()).thenReturn(ScooterStatus.TAKEN);

        when(this.scooterRepository.findById(2L)).thenReturn(Optional.of(scooter));

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.rentalStarter.startRental(userId, scooterId, rentalPointId, billingMode)
        );
        assertEquals("Scooter with id: 2 is not free.", ex.getMessage());

        verify(this.scooterRepository, times(1)).findById(scooterId);
        verify(this.rentalRepository, never()).save(any());
    }

    @Test
    void startRental_shouldThrowUserNotFoundException_whenUserNotFound() {
        // Arrange
        Long userId = 1L;
        Long scooterId = 2L;
        Long rentalPointId = 3L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        Scooter scooter = mock(Scooter.class);
        when(scooter.getStatus()).thenReturn(ScooterStatus.FREE);

        when(this.scooterRepository.findById(scooterId)).thenReturn(Optional.of(scooter));
        when(this.userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> this.rentalStarter.startRental(userId, scooterId, rentalPointId, billingMode));
        assertEquals("User with id: 1 not found.", ex.getMessage());

        verify(this.scooterRepository, times(1)).findById(scooterId);
        verify(this.userRepository, times(1)).findById(userId);
        verify(this.rentalRepository, never()).save(any());
    }

    @Test
    void startRental_shouldThrowNotFoundException_whenLocationNotFound() {
        // Arrange
        Long userId = 1L;
        Long scooterId = 2L;
        Long rentalPointId = 3L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        Scooter scooter = mock(Scooter.class);
        when(scooter.getStatus()).thenReturn(ScooterStatus.FREE);

        when(this.scooterRepository.findById(scooterId)).thenReturn(Optional.of(scooter));
        when(this.userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
        when(this.locationRepository.findById(rentalPointId)).thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.rentalStarter.startRental(userId, scooterId, rentalPointId, billingMode));
        assertEquals("Rental point with id: 3 not found.", ex.getMessage());

        verify(this.scooterRepository, times(1)).findById(scooterId);
        verify(this.userRepository, times(1)).findById(userId);
        verify(this.locationRepository, times(1)).findById(rentalPointId);
        verify(this.rentalRepository, never()).save(any());
    }

    @Test
    void startRental_shouldThrowBusinessLogicException_whenLocationNotRentalPoint() {
        // Arrange
        Long userId = 1L;
        Long scooterId = 2L;
        Long rentalPointId = 3L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        Scooter scooter = mock(Scooter.class);
        when(scooter.getStatus()).thenReturn(ScooterStatus.FREE);

        LocationNode location = mock(LocationNode.class);
        when(location.getLocationType()).thenReturn(LocationType.CITY);

        when(this.scooterRepository.findById(scooterId)).thenReturn(Optional.of(scooter));
        when(this.userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
        when(this.locationRepository.findById(rentalPointId)).thenReturn(Optional.of(location));

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.rentalStarter.startRental(userId, scooterId, rentalPointId, billingMode));

        assertEquals("Location is not a rental point.", ex.getMessage());

        verify(this.scooterRepository, times(1)).findById(scooterId);
        verify(this.userRepository, times(1)).findById(userId);
        verify(this.locationRepository, times(1)).findById(rentalPointId);
        verify(this.rentalRepository, never()).save(any());
    }

    @Test
    void startRental_shouldThrowBusinessLogicException_whenUserHasActiveRental() {
        // Arrange
        Long userId = 1L;
        Long scooterId = 2L;
        Long rentalPointId = 3L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        Scooter scooter = mock(Scooter.class);
        when(scooter.getStatus()).thenReturn(ScooterStatus.FREE);

        LocationNode location = mock(LocationNode.class);
        when(location.getLocationType()).thenReturn(LocationType.RENTAL_POINT);

        when(this.scooterRepository.findById(scooterId)).thenReturn(Optional.of(scooter));
        when(this.userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
        when(this.locationRepository.findById(rentalPointId)).thenReturn(Optional.of(location));
        when(this.rentalRepository.isActiveRentalByUserId(userId)).thenReturn(true);

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.rentalStarter.startRental(userId, scooterId, rentalPointId, billingMode));
        assertEquals("Rental for user with id: 1 is already active.", ex.getMessage());

        verify(this.scooterRepository, times(1)).findById(scooterId);
        verify(this.userRepository, times(1)).findById(userId);
        verify(this.locationRepository, times(1)).findById(rentalPointId);
        verify(this.rentalRepository, times(1)).isActiveRentalByUserId(userId);
        verify(this.rentalRepository, never()).save(any());
    }

    @Test
    void startRental_shouldThrowBusinessLogicException_whenScooterNotAtPoint() {
        // Arrange
        Long userId = 1L;
        Long scooterId = 2L;
        Long rentalPointId = 3L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        Scooter scooter = mock(Scooter.class);
        when(scooter.getStatus()).thenReturn(ScooterStatus.FREE);
        when(scooter.getId()).thenReturn(2L);

        LocationNode point = mock(LocationNode.class);
        when(point.getLocationType()).thenReturn(LocationType.RENTAL_POINT);
        when(point.getId()).thenReturn(rentalPointId);
        when(point.getScooters()).thenReturn(Set.of());

        when(this.scooterRepository.findById(scooterId)).thenReturn(Optional.of(scooter));
        when(this.userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
        when(this.locationRepository.findById(rentalPointId)).thenReturn(Optional.of(point));
        when(this.rentalRepository.isActiveRentalByUserId(1L)).thenReturn(false);

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.rentalStarter.startRental(userId, scooterId, rentalPointId, billingMode));
        assertEquals("Scooter with id: 2 is not at the rental point with id: 3", ex.getMessage());

        verify(this.scooterRepository, times(1)).findById(scooterId);
        verify(this.userRepository, times(1)).findById(userId);
        verify(this.locationRepository, times(1)).findById(rentalPointId);
        verify(this.rentalRepository, times(1)).isActiveRentalByUserId(userId);
        verify(this.rentalRepository, never()).save(any());
    }

    @Test
    void startRental_shouldThrowServiceException_whenTariffSelectFails() {
        // Arrange
        Long userId = 1L;
        Long scooterId = 2L;
        Long rentalPointId = 3L;
        BillingMode billingMode = BillingMode.PER_HOUR;

        Scooter scooter = mock(Scooter.class);
        LocationNode point = mock(LocationNode.class);

        when(scooter.getStatus()).thenReturn(ScooterStatus.FREE);
        when(point.getLocationType()).thenReturn(LocationType.RENTAL_POINT);
        when(point.getScooters()).thenReturn(Set.of(scooter));

        when(this.scooterRepository.findById(scooterId)).thenReturn(Optional.of(scooter));
        when(this.userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
        when(this.locationRepository.findById(rentalPointId)).thenReturn(Optional.of(point));
        when(this.rentalRepository.isActiveRentalByUserId(userId)).thenReturn(false);
        when(this.tariffSelectionService.selectTariffForUser(userId, billingMode))
                .thenThrow(new ServiceException("Something went wrong"));

        // Act | Assert
        ServiceException ex = assertThrows(ServiceException.class,
                () -> this.rentalStarter.startRental(userId, scooterId, rentalPointId, billingMode));
        assertEquals("Error in TariffSelectionService for user with id: 1", ex.getMessage());

        verify(this.scooterRepository, times(1)).findById(scooterId);
        verify(this.userRepository, times(1)).findById(userId);
        verify(this.locationRepository, times(1)).findById(rentalPointId);
        verify(this.rentalRepository, times(1)).isActiveRentalByUserId(userId);
        verify(this.tariffSelectionService, times(1)).selectTariffForUser(userId, billingMode);
        verify(this.rentalRepository, never()).save(any());
    }
}