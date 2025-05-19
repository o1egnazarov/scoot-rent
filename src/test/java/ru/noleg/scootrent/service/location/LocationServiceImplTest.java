package ru.noleg.scootrent.service.location;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.location.LocationType;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.repository.LocationRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void getAllLocations_shouldBuildHierarchyCorrectly() {
        // Arrange
        LocationNode country = new LocationNode();
        country.setId(1L);
        country.setParent(null);
        country.setChildren(new ArrayList<>());

        LocationNode city = new LocationNode();
        city.setId(2L);
        city.setParent(country);
        city.setChildren(new ArrayList<>());

        LocationNode district = new LocationNode();
        district.setId(3L);
        district.setParent(city);
        district.setChildren(new ArrayList<>());

        List<LocationNode> allNodes = List.of(country, city, district);
        when(this.locationRepository.findAll()).thenReturn(allNodes);

        // Act
        List<LocationNode> result = this.locationService.getAllLocations();

        // Assert
        assertEquals(1, result.size());
        assertEquals(country, result.get(0));
        assertEquals(1, country.getChildren().size());
        assertEquals(city, country.getChildren().get(0));
        assertEquals(1, city.getChildren().size());
        assertEquals(district, city.getChildren().get(0));
        assertTrue(district.getChildren().isEmpty());

        verify(this.locationRepository, times(1)).findAll();
    }

    @Test
    void getAllLocations_shouldTreatNodeAsRoot_whenParentIsNull() {
        // Arrange
        LocationNode root = new LocationNode();
        root.setId(1L);
        root.setParent(null);
        root.setChildren(new ArrayList<>());

        when(this.locationRepository.findAll()).thenReturn(List.of(root));

        // Act
        List<LocationNode> result = this.locationService.getAllLocations();

        // Assert
        assertEquals(1, result.size());
        assertEquals(root, result.get(0));
        assertTrue(root.getChildren().isEmpty());

        verify(this.locationRepository, times(1)).findAll();
    }

    @Test
    void getAllLocations_shouldTreatNodeAsRoot_whenParentIdIsNull() {
        // Arrange
        LocationNode parentLocation = new LocationNode();
        LocationNode location = new LocationNode();
        location.setId(1L);
        location.setParent(parentLocation);
        location.setChildren(new ArrayList<>());

        when(this.locationRepository.findAll()).thenReturn(List.of(location));

        // Act
        List<LocationNode> result = this.locationService.getAllLocations();

        // Assert
        assertEquals(1, result.size());
        assertEquals(location, result.get(0));
        assertTrue(location.getChildren().isEmpty());

        verify(this.locationRepository, times(1)).findAll();
    }

    @Test
    void getAllLocations_shouldReturnEmptyList_whenNoLocationsFound() {
        // Arrange
        when(this.locationRepository.findAll()).thenReturn(List.of());

        // Act
        List<LocationNode> result = this.locationService.getAllLocations();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(this.locationRepository, times(1)).findAll();
    }

    @Test
    void getAllLocations_shouldThrowServiceException_whenRepositoryFails() {
        // Arrange
        when(this.locationRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        // Act | Assert
        ServiceException ex = assertThrows(ServiceException.class,
                () -> this.locationService.getAllLocations()
        );
        assertTrue(ex.getMessage().contains("Error on get all locations."));

        verify(this.locationRepository, times(1)).findAll();
    }
}