package ru.noleg.scootrent.dto.location;

import ru.noleg.scootrent.entity.location.LocationType;

import java.math.BigDecimal;
import java.util.List;

public record LocationDto(Long id,
                          String title,
                          LocationType locationType,
                          BigDecimal latitude,
                          BigDecimal longitude,
                          String address,
                          List<LocationDto> children) {
}
