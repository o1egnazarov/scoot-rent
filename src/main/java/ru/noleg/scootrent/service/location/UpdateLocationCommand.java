package ru.noleg.scootrent.service.location;

import ru.noleg.scootrent.entity.location.LocationType;

import java.math.BigDecimal;

public record UpdateLocationCommand(
        String address,
        LocationType locationType,
        String title,
        BigDecimal latitude,
        BigDecimal longitude,
        Long parentId
) {
}
