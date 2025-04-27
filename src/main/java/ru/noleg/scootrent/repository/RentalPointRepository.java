package ru.noleg.scootrent.repository;

import ru.noleg.scootrent.entity.rental.RentalPoint;
import ru.noleg.scootrent.repository.util.BaseRepository;

import java.util.List;

public interface RentalPointRepository extends BaseRepository<RentalPoint, Long> {
    List<RentalPoint> findAllRentalPointByDistrict(Long countryId, Long cityId, Long districtId);
}
