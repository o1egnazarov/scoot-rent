package ru.noleg.scootrent.service.location;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.location.LocationType;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.LocationRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationServiceImpl locationService;

    @Test
    void add_shouldSaveLocation_whenParentExists() {
        // Arrange
        Long parentId = 10L;
        LocationNode parent = mock(LocationNode.class);
        when(parent.getId()).thenReturn(parentId);

        LocationNode locationToAdd = mock(LocationNode.class);
        when(locationToAdd.getParent()).thenReturn(parent);

        when(this.locationRepository.existsById(parentId)).thenReturn(true);

        Long locationId = 5L;
        LocationNode savedLocation = mock(LocationNode.class);
        when(savedLocation.getId()).thenReturn(locationId);

        when(this.locationRepository.save(locationToAdd)).thenReturn(savedLocation);

        // Act
        Long resultId = this.locationService.add(locationToAdd);

        // Assert
        assertEquals(locationId, resultId);

        verify(this.locationRepository, times(1)).existsById(parentId);
        verify(this.locationRepository, times(1)).save(locationToAdd);
    }

    @Test
    void add_shouldThrowNotFoundException_whenParentNotFound() {
        // Arrange
        Long missingParentId = 100L;
        LocationNode parent = mock(LocationNode.class);
        when(parent.getId()).thenReturn(missingParentId);

        LocationNode location = mock(LocationNode.class);
        when(location.getParent()).thenReturn(parent);

        when(this.locationRepository.existsById(missingParentId)).thenReturn(false);

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.locationService.add(location)
        );
        assertEquals("Parent location with id 100 does not exist.", ex.getMessage());

        verify(this.locationRepository, times(1)).existsById(missingParentId);
        verify(this.locationRepository, never()).save(any());
    }

    @Test
    void delete_shouldDelete_whenExists() {
        // Arrange
        Long locationId = 1L;
        when(this.locationRepository.existsById(locationId)).thenReturn(true);

        // Act
        this.locationService.delete(locationId);

        // Assert
        verify(this.locationRepository, times(1)).delete(locationId);
    }

    @Test
    void delete_shouldThrowNotFoundException_whenNotExists() {
        // Arrange
        Long locationId = 100L;
        when(this.locationRepository.existsById(locationId)).thenReturn(false);

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.locationService.delete(locationId)
        );
        assertEquals("Location with id: 100 not found.", ex.getMessage());

        verify(this.locationRepository, never()).delete(any());
    }

    @Test
    void getLocationById_shouldReturnLocation_whenExists() {
        // Arrange
        Long locationId = 1L;
        LocationNode location = mock(LocationNode.class);

        when(this.locationRepository.findLocationById(locationId)).thenReturn(Optional.of(location));

        // Act
        LocationNode result = this.locationService.getLocationById(locationId);

        // Assert
        assertEquals(location, result);

        verify(this.locationRepository, times(1)).findLocationById(locationId);
    }

    @Test
    void getLocationById_shouldThrowNotFoundException_whenNotExists() {
        // Arrange
        Long locationId = 100L;
        when(this.locationRepository.findLocationById(locationId)).thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.locationService.getLocationById(locationId)
        );
        assertEquals("Location with id: 100 not found.", ex.getMessage());

        verify(this.locationRepository, times(1)).findLocationById(locationId);
    }

    @Test
    void getLocationByIdAndType_shouldReturnNode_whenExists() {
        // Arrange
        Long locationId = 1L;
        LocationType locationType = LocationType.CITY;
        LocationNode location = mock(LocationNode.class);

        when(this.locationRepository.findLocationByIdAndType(locationId, locationType))
                .thenReturn(Optional.of(location));

        // Act
        LocationNode result = this.locationService.getLocationByIdAndType(locationId, locationType);

        // Assert
        assertEquals(location, result);

        verify(this.locationRepository, times(1))
                .findLocationByIdAndType(locationId, locationType);
    }

    @Test
    void getLocationByIdAndType_shouldThrowNotFoundException_whenNotExists() {
        // Arrange
        Long locationId = 100L;
        LocationType locationType = LocationType.CITY;

        when(this.locationRepository.findLocationByIdAndType(locationId, locationType)).thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.locationService.getLocationByIdAndType(locationId, locationType)
        );
        assertEquals("Location with id: 100 and type: CITY - not found.", ex.getMessage());

        verify(this.locationRepository, times(1))
                .findLocationByIdAndType(locationId, locationType);
    }

    @Test
    void getLocationByCoordinatesAndType_shouldReturnNode_whenExists() {
        // Arrange
        LocationType locationType = LocationType.CITY;
        LocationNode node = mock(LocationNode.class);
        BigDecimal latitude = new BigDecimal("56.465390");
        BigDecimal longitude = new BigDecimal("84.950164");

        when(this.locationRepository.findLocationByCoordinatesAndType(latitude, longitude, locationType))
                .thenReturn(Optional.of(node));

        // Act
        LocationNode result = this.locationService.getLocationByCoordinatesAndType(latitude, longitude, locationType);

        // Assert
        assertEquals(node, result);
        verify(this.locationRepository, times(1))
                .findLocationByCoordinatesAndType(latitude, longitude, locationType);
    }

    @Test
    void getLocationByCoordinatesAndType_shouldThrowNotFoundException_whenNotExists() {
        // Arrange
        LocationType locationType = LocationType.CITY;
        BigDecimal latitude = new BigDecimal("56.465390");
        BigDecimal longitude = new BigDecimal("84.950164");

        when(this.locationRepository.findLocationByCoordinatesAndType(latitude, longitude, locationType))
                .thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> this.locationService.getLocationByCoordinatesAndType(latitude, longitude, locationType)
        );
        assertEquals("Rental point with latitude: 56.465390 and longitude: 84.950164 and type: CITY not found.",
                ex.getMessage());

        verify(this.locationRepository, times(1))
                .findLocationByCoordinatesAndType(latitude, longitude, locationType);
    }

    @Test
    void getChildrenLocation_shouldReturnList() {
        // Arrange
        Long parentId = 1L;
        LocationNode child = mock(LocationNode.class);

        when(this.locationRepository.findChildrenLocationByParentId(parentId)).thenReturn(List.of(child));

        // Act
        List<LocationNode> result = this.locationService.getChildrenLocation(parentId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(child, result.get(0));

        verify(this.locationRepository, times(1)).findChildrenLocationByParentId(parentId);
    }

//    @Test
//    void getAllLocations_shouldReturnListLocation() {
//        // Arrange
//        LocationNode root = mock(LocationNode.class);
//        LocationNode child = mock(LocationNode.class);
//
//        when(root.getChildren()).thenReturn(List.of(child));
//        when(child.getParent()).thenReturn(root);
//        when(child.getChildren()).thenReturn(List.of());
//
//        when(this.locationRepository.findAll()).thenReturn(List.of(root, child));
//
//        // Act
//        List<LocationNode> result = this.locationService.getAllLocations();
//
//        // Assert
//        assertEquals(1, result.size());
//        assertEquals(1L, result.get(0).getId());
//        assertEquals(1, result.get(0).getChildren().size());
//        assertEquals(2L, result.get(0).getChildren().get(0).getId());
//
//        verify(this.locationRepository, times(1)).findAll();
//    }
}