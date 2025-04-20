package ru.noleg.scootrent.service;

import ru.noleg.scootrent.entity.rental.RentalPoint;

import java.util.List;

public interface RentalPointService {
    Long add(RentalPoint rentalPoint);

    void delete(Long id);

    RentalPoint getRentalPoint(Long id);

    List<RentalPoint> getAllRentalPoints();

    RentalPoint getRentalPointByCoordinates(double latitude, double longitude);
}
