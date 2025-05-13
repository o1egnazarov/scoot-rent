package ru.noleg.scootrent.repository;

import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.location.LocationType;
import ru.noleg.scootrent.repository.util.BaseRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LocationRepository extends BaseRepository<LocationNode, Long> {
    Optional<LocationNode> findLocationById(Long id);

    Optional<LocationNode> findLocationByIdAndType(Long id, LocationType type);

    Optional<LocationNode> findLocationByCoordinatesAndType(BigDecimal latitude, BigDecimal longitude, LocationType type);

    List<LocationNode> findChildrenLocationByParentId(Long parentId);
}
