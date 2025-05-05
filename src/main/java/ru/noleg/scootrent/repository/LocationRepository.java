package ru.noleg.scootrent.repository;

import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.repository.util.BaseRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LocationRepository extends BaseRepository<LocationNode, Long> {
    Optional<LocationNode> findLocationByCoordinates(BigDecimal latitude, BigDecimal longitude);

    Optional<LocationNode> findLocationById(Long id);

    List<LocationNode> findChildrenLocationByParentId(Long parentId);
}
