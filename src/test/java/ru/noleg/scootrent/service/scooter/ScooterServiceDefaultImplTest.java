package ru.noleg.scootrent.service.scooter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.location.LocationType;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.entity.scooter.ScooterModel;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.LocationRepository;
import ru.noleg.scootrent.repository.ScooterModelRepository;
import ru.noleg.scootrent.repository.ScooterRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScooterServiceDefaultImplTest {

    @Mock
    private ScooterRepository scooterRepository;

    @Mock
    private ScooterModelRepository scooterModelRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private ScooterServiceDefaultImpl scooterService;

    @Test
    void add_shouldSaveScooterAndReturnId_whenValidModelAndRentalPoint() {
        // Arrange
        ScooterModel model = new ScooterModel();
        model.setId(1L);

        LocationNode rentalPoint = new LocationNode();
        rentalPoint.setId(2L);
        rentalPoint.setLocationType(LocationType.RENTAL_POINT);

        Scooter scooter = new Scooter();
        scooter.setNumberPlate("ABC123");
        scooter.setModel(model);
        scooter.setRentalPoint(rentalPoint);

        Scooter savedScooter = new Scooter();
        savedScooter.setId(10L);

        when(this.scooterModelRepository.existsById(1L)).thenReturn(true);
        when(this.locationRepository.findLocationById(2L)).thenReturn(Optional.of(rentalPoint));
        when(this.scooterRepository.save(scooter)).thenReturn(savedScooter);

        // Act
        Long result = scooterService.add(scooter);

        // Assert
        assertEquals(10L, result);
        verify(this.scooterModelRepository, times(1)).existsById(1L);
        verify(this.locationRepository, times(1)).findLocationById(2L);
        verify(this.scooterRepository, times(1)).save(scooter);
    }

    @Test
    void add_shouldThrowNotFoundException_whenScooterModelNotExists() {
        // Arrange
        ScooterModel model = new ScooterModel();
        model.setId(100L);

        Scooter scooter = new Scooter();
        scooter.setModel(model);
        scooter.setRentalPoint(new LocationNode());

        when(this.scooterModelRepository.existsById(100L)).thenReturn(false);

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> scooterService.add(scooter));
        assertEquals("Scooter model with id 100 not found.", ex.getMessage());

        verify(this.scooterModelRepository, times(1)).existsById(100L);
        verify(this.locationRepository, never()).findLocationById(any());
        verify(this.scooterRepository, never()).save(any());
    }

    @Test
    void add_shouldThrowNotFoundException_whenRentalPointNotFound() {
        // Arrange
        ScooterModel model = new ScooterModel();
        model.setId(1L);

        LocationNode rentalPoint = new LocationNode();
        rentalPoint.setId(100L);

        Scooter scooter = new Scooter();
        scooter.setModel(model);
        scooter.setRentalPoint(rentalPoint);

        when(this.scooterModelRepository.existsById(1L)).thenReturn(true);
        when(this.locationRepository.findLocationById(100L)).thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> scooterService.add(scooter));
        assertEquals("Location with id 100 not found.", ex.getMessage());

        verify(this.scooterModelRepository, times(1)).existsById(1L);
        verify(this.locationRepository, times(1)).findLocationById(100L);
        verify(this.scooterRepository, never()).save(any());
    }

    @Test
    void add_shouldThrowBusinessLogicException_whenLocationIsNotRentalPoint() {
        // Arrange
        ScooterModel model = new ScooterModel();
        model.setId(1L);

        LocationNode location = new LocationNode();
        location.setId(2L);
        location.setLocationType(LocationType.CITY);

        Scooter scooter = new Scooter();
        scooter.setModel(model);
        scooter.setRentalPoint(location);

        when(this.scooterModelRepository.existsById(1L)).thenReturn(true);
        when(this.locationRepository.findLocationById(2L)).thenReturn(Optional.of(location));

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class, () -> scooterService.add(scooter));
        assertEquals("Location is not a rental point.", ex.getMessage());

        verify(this.scooterModelRepository, times(1)).existsById(1L);
        verify(this.locationRepository, times(1)).findLocationById(2L);
        verify(this.scooterRepository, never()).save(any());
    }

    @Test
    void delete_shouldRemoveScooter_whenExists() {
        // Arrange
        Long id = 1L;
        when(this.scooterRepository.existsById(id)).thenReturn(true);
        doNothing().when(this.scooterRepository).delete(id);

        // Act
        this.scooterService.delete(id);

        // Assert
        verify(this.scooterRepository, times(1)).existsById(id);
        verify(this.scooterRepository, times(1)).delete(id);
    }

    @Test
    void delete_shouldThrowNotFoundException_whenScooterNotExists() {
        // Arrange
        when(this.scooterRepository.existsById(100L)).thenReturn(false);

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> scooterService.delete(100L));
        assertEquals("Scooter with id 100 not found.", ex.getMessage());

        verify(this.scooterRepository, never()).delete(any());
    }

    @Test
    void getScooter_shouldReturnScooter_whenExists() {
        // Arrange
        Scooter scooter = new Scooter();
        scooter.setId(1L);
        when(this.scooterRepository.findById(1L)).thenReturn(Optional.of(scooter));

        // Act
        Scooter result = this.scooterService.getScooter(1L);

        // Assert
        assertEquals(1L, result.getId());
        verify(this.scooterRepository).findById(1L);
    }

    @Test
    void getScooter_shouldThrowNotFoundException_whenNotExists() {
        // Arrange
        when(this.scooterRepository.findById(100L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> this.scooterService.getScooter(100L));
        assertEquals("Scooter with id 100 not found.", ex.getMessage());
    }

    @Test
    void getAllScooters_shouldReturnListOfScooters() {
        // Arrange
        List<Scooter> scooters = List.of(new Scooter(), new Scooter());
        when(this.scooterRepository.findAll()).thenReturn(scooters);

        // Act
        List<Scooter> result = this.scooterService.getAllScooters();

        // Assert
        assertEquals(2, result.size());
        verify(this.scooterRepository, times(1)).findAll();
    }
}