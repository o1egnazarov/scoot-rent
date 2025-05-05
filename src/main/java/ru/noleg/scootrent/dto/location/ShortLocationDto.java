package ru.noleg.scootrent.dto.location;

import ru.noleg.scootrent.entity.location.LocationType;

public record ShortLocationDto(Long id,
                               String title,
                               LocationType locationType,
                               String address) {
}
