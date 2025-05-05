package ru.noleg.scootrent.service;

import jakarta.validation.constraints.Min;
import ru.noleg.scootrent.entity.location.LocationNode;

import java.math.BigDecimal;
import java.util.List;

public interface LocationService {
    Long add(LocationNode locationNode);

    void delete(Long id);

    LocationNode getLocationById(Long id);

    List<LocationNode> getAllLocations();

    LocationNode getLocationByCoordinates(BigDecimal latitude, BigDecimal longitude);

    List<LocationNode> getChildrenLocation(Long parentId);
}
