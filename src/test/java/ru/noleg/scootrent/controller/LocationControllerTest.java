package ru.noleg.scootrent.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.noleg.scootrent.controller.location.LocationController;
import ru.noleg.scootrent.dto.location.CreateLocationDto;
import ru.noleg.scootrent.dto.location.DetailLocationDto;
import ru.noleg.scootrent.dto.location.LocationDto;
import ru.noleg.scootrent.dto.location.UpdateLocationDto;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.location.LocationType;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.RepositoryException;
import ru.noleg.scootrent.mapper.LocationMapper;
import ru.noleg.scootrent.service.location.LocationService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {

    @Mock
    private LocationMapper locationMapper;
    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    @Test
    void addTariff_shouldReturnCreatedId() {
        // Arrange
        Long locationId = 1L;
        CreateLocationDto locationDto = mock(CreateLocationDto.class);
        LocationNode location = mock(LocationNode.class);

        when(this.locationMapper.mapToEntity(locationDto)).thenReturn(location);
        when(this.locationService.add(location)).thenReturn(locationId);

        // Act
        ResponseEntity<Long> response = this.locationController.addLocation(locationDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(locationId, response.getBody());

        verify(this.locationMapper, times(1)).mapToEntity(locationDto);
        verify(this.locationService, times(1)).add(location);
    }

    @Test
    void addLocation_shouldThrownException_whenDbError() {
        // Arrange
        CreateLocationDto dto = mock(CreateLocationDto.class);
        LocationNode node = mock(LocationNode.class);

        when(this.locationMapper.mapToEntity(dto)).thenReturn(node);
        when(this.locationService.add(node))
                .thenThrow(new RepositoryException("Database error"));

        // Act | Assert
        RepositoryException exception = assertThrows(RepositoryException.class,
                () -> this.locationController.addLocation(dto)
        );

        assertEquals("Database error", exception.getMessage());

        verify(this.locationMapper, times(1)).mapToEntity(dto);
        verify(this.locationService, times(1)).add(node);
    }

    @Test
    void updateLocation_shouldUpdateAndReturnDto() {
        // Arrange
        Long id = 1L;
        UpdateLocationDto dto = mock(UpdateLocationDto.class);
        LocationNode locationNode = mock(LocationNode.class);
        DetailLocationDto resultDto = mock(DetailLocationDto.class);

        when(this.locationService.getLocationById(id)).thenReturn(locationNode);
        doNothing().when(this.locationMapper).updateRentalPointFromDto(dto, locationNode);
        when(this.locationService.add(locationNode)).thenReturn(id);
        when(this.locationMapper.mapToDetailDto(locationNode)).thenReturn(resultDto);

        // Act
        ResponseEntity<DetailLocationDto> response = this.locationController.updateLocation(id, dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resultDto, response.getBody());

        verify(this.locationService, times(1)).getLocationById(id);
        verify(this.locationMapper, times(1)).updateRentalPointFromDto(dto, locationNode);
        verify(this.locationService, times(1)).add(locationNode);
        verify(this.locationMapper, times(1)).mapToDetailDto(locationNode);
    }

    @Test
    void updateLocation_shouldThrownException_whenLocationNotFound() {
        // Arrange
        Long locationId = 100L;
        UpdateLocationDto dto = mock(UpdateLocationDto.class);

        when(this.locationService.getLocationById(locationId))
                .thenThrow(new NotFoundException("Location not found"));

        // Act | Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.locationController.updateLocation(locationId, dto)
        );

        assertEquals("Location not found", exception.getMessage());

        verify(this.locationService, times(1)).getLocationById(locationId);
        verifyNoMoreInteractions(this.locationService);
    }

    @Test
    void deleteLocation_shouldReturnOk() {
        // Arrange
        Long id = 1L;
        doNothing().when(this.locationService).delete(id);

        // Act
        ResponseEntity<Void> response = this.locationController.deleteLocation(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        verify(this.locationService, times(1)).delete(id);
    }

    @Test
    void deleteLocation_shouldThrowNotFound_whenLocationDoesNotExist() {
        // Arrange
        Long locationId = 100L;
        doThrow(new NotFoundException("Location not found")).when(this.locationService).delete(locationId);

        // Act | Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.locationController.deleteLocation(locationId)
        );

        assertEquals("Location not found", exception.getMessage());

        verify(this.locationService, times(1)).delete(locationId);
        verifyNoMoreInteractions(this.locationService);
    }

    @Test
    void getAllLocation_shouldReturnAllLocations() {
        // Arrange
        List<LocationNode> nodes = List.of(mock(LocationNode.class), mock(LocationNode.class));
        List<LocationDto> dtos = List.of(mock(LocationDto.class), mock(LocationDto.class));

        when(this.locationService.getAllLocations()).thenReturn(nodes);
        when(this.locationMapper.mapToDtos(nodes)).thenReturn(dtos);

        // Act
        ResponseEntity<List<LocationDto>> response = this.locationController.getAllLocation();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dtos, response.getBody());

        verify(this.locationService).getAllLocations();
        verify(this.locationMapper).mapToDtos(nodes);
    }

    @Test
    void getAllLocation_shouldThrownException_whenDbError() {
        // Arrange
        when(this.locationService.getAllLocations())
                .thenThrow(new RepositoryException("Database error"));

        // Act | Assert
        RepositoryException exception = assertThrows(RepositoryException.class,
                () -> this.locationController.getAllLocation()
        );

        assertEquals("Database error", exception.getMessage());

        verify(this.locationService, times(1)).getAllLocations();
        verifyNoMoreInteractions(this.locationService);
    }

    @Test
    void getChildrenLocation_shouldReturnChildrenDtos() {
        // Arrange
        Long parentId = 2L;
        List<LocationNode> children = List.of(mock(LocationNode.class));
        List<DetailLocationDto> childrenDtos = List.of(mock(DetailLocationDto.class));

        when(this.locationService.getChildrenLocation(parentId)).thenReturn(children);
        when(this.locationMapper.mapToDetailDtos(children)).thenReturn(childrenDtos);

        // Act
        ResponseEntity<List<DetailLocationDto>> response = this.locationController.getChildrenLocation(parentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(childrenDtos, response.getBody());

        verify(this.locationService, times(1)).getChildrenLocation(parentId);
        verify(this.locationMapper, times(1)).mapToDetailDtos(children);
    }

    @Test
    void getChildrenLocation_shouldThrownException_whenDbError() {
        // Arrange
        Long parentId = 100L;
        when(this.locationService.getChildrenLocation(parentId))
                .thenThrow(new RepositoryException("Database error"));

        // Act | Assert
        RepositoryException exception = assertThrows(RepositoryException.class,
                () -> this.locationController.getChildrenLocation(parentId)
        );

        assertEquals("Database error", exception.getMessage());

        verify(this.locationService, times(1)).getChildrenLocation(parentId);
        verifyNoMoreInteractions(this.locationService);
    }


    @Test
    void getRentalPointById_shouldReturnRentalPointDto() {
        // Arrange
        Long id = 1L;
        LocationNode node = mock(LocationNode.class);
        DetailLocationDto dto = mock(DetailLocationDto.class);

        when(this.locationService.getLocationByIdAndType(id, LocationType.RENTAL_POINT)).thenReturn(node);
        when(this.locationMapper.mapToDetailDto(node)).thenReturn(dto);

        // Act
        ResponseEntity<DetailLocationDto> response = this.locationController.getRentalPointById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());

        verify(this.locationService).getLocationByIdAndType(id, LocationType.RENTAL_POINT);
        verify(this.locationMapper).mapToDetailDto(node);
    }

    @Test
    void getRentalPointById_shouldThrownException_whenLocationDoesNotExist() {
        // Arrange
        Long locationId = 100L;
        when(this.locationService.getLocationByIdAndType(locationId, LocationType.RENTAL_POINT))
                .thenThrow(new NotFoundException("Rental point not found"));

        // Act | Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.locationController.getRentalPointById(locationId)
        );

        assertEquals("Rental point not found", exception.getMessage());

        verify(this.locationService, times(1))
                .getLocationByIdAndType(locationId, LocationType.RENTAL_POINT);
        verifyNoMoreInteractions(this.locationService);
    }

    @Test
    void getRentalPointByCoordinates_shouldReturnRentalPointDto() {
        // Arrange
        BigDecimal latitude = new BigDecimal("56.465390");
        BigDecimal longitude = new BigDecimal("84.950164");
        LocationNode node = mock(LocationNode.class);
        DetailLocationDto dto = mock(DetailLocationDto.class);

        when(this.locationService.getLocationByCoordinatesAndType(latitude, longitude, LocationType.RENTAL_POINT)).thenReturn(node);
        when(this.locationMapper.mapToDetailDto(node)).thenReturn(dto);
        when(node.getId()).thenReturn(1L);

        // Act
        ResponseEntity<DetailLocationDto> response = this.locationController.getRentalPointByCoordinates(latitude, longitude);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());

        verify(this.locationService).getLocationByCoordinatesAndType(latitude, longitude, LocationType.RENTAL_POINT);
        verify(this.locationMapper).mapToDetailDto(node);
    }

    @Test
    void getRentalPointByCoordinates_shouldThrownException_whenNoRentalPointAtCoordinates() {
        // Arrange
        BigDecimal latitude = new BigDecimal("0.0");
        BigDecimal longitude = new BigDecimal("0.0");

        when(this.locationService.getLocationByCoordinatesAndType(latitude, longitude, LocationType.RENTAL_POINT))
                .thenThrow(new NotFoundException("No rental point at given coordinates"));

        // Act | Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.locationController.getRentalPointByCoordinates(latitude, longitude)
        );

        assertEquals("No rental point at given coordinates", exception.getMessage());

        verify(this.locationService, times(1))
                .getLocationByCoordinatesAndType(latitude, longitude, LocationType.RENTAL_POINT);
        verifyNoMoreInteractions(this.locationService);
    }
}