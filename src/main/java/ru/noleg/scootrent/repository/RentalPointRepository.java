package ru.noleg.scootrent.repository;

import ru.noleg.scootrent.entity.rental.RentalPoint;
import ru.noleg.scootrent.repository.util.BaseRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RentalPointRepository extends BaseRepository<RentalPoint, Long> {
    List<RentalPoint> findAllRentalPointByDistrict(Long countryId, Long cityId, Long districtId);

    Optional<RentalPoint> findRentalPointByCoordinates(BigDecimal latitude, BigDecimal longitude);

    List<RentalPoint> findAllRentalPoints();

    Optional<RentalPoint> findRentalPointById(Long id);
}
