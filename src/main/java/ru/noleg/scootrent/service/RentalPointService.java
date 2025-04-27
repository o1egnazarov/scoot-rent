package ru.noleg.scootrent.service;

import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.rental.RentalPoint;

import java.math.BigDecimal;
import java.util.List;

public interface RentalPointService {
    Long add(RentalPoint rentalPoint);

    void delete(Long id);

    RentalPoint getRentalPoint(Long id);

    List<RentalPoint> getAllRentalPoints();

    List<RentalPoint> getRentalPointsByDistrict(Long countryId, Long cityId ,Long districtId);

    RentalPoint getRentalPointByCoordinates(BigDecimal latitude, BigDecimal longitude);
}
