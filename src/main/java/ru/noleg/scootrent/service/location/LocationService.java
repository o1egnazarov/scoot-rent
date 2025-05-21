package ru.noleg.scootrent.service.location;

import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.location.LocationType;

import java.math.BigDecimal;
import java.util.List;

public interface LocationService {
    Long add(LocationNode locationNode);

    LocationNode update(Long id, UpdateLocationCommand updateLocationCommand);

    void delete(Long id);

    LocationNode getLocationById(Long id);

    LocationNode getLocationByIdAndType(Long id, LocationType type);

    List<LocationNode> getAllLocations();

    LocationNode getLocationByCoordinatesAndType(BigDecimal latitude, BigDecimal longitude, LocationType type);

    List<LocationNode> getChildrenLocation(Long parentId);
}
