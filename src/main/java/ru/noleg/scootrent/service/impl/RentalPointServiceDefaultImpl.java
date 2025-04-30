package ru.noleg.scootrent.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.rental.RentalPoint;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.repository.RentalPointRepository;
import ru.noleg.scootrent.service.RentalPointService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RentalPointServiceDefaultImpl implements RentalPointService {

    private final RentalPointRepository rentalPointRepository;

    public RentalPointServiceDefaultImpl(RentalPointRepository rentalPointRepository) {
        this.rentalPointRepository = rentalPointRepository;
    }

    @Override
    @Transactional
    public Long add(RentalPoint rentalPoint) {
        try {

            return this.rentalPointRepository.save(rentalPoint).getId();
        } catch (Exception e) {

            throw new ServiceException("Error on add rental point.", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {

            if (!this.rentalPointRepository.existsById(id)) {
                throw new NotFoundException("Rental point with id: " + id + " not found.");
            }

            this.rentalPointRepository.delete(id);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error on delete rental point.", e);
        }
    }

    @Override
    public RentalPoint getRentalPoint(Long id) {
        try {

            return this.rentalPointRepository.findRentalPointById(id).orElseThrow(
                    () -> new NotFoundException("Rental point with id " + id + " not found")
            );
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error on get rentalPoint.", e);
        }
    }

    @Override
    public List<RentalPoint> getAllRentalPoints() {
        try {
            return this.rentalPointRepository.findAllRentalPoints();
        } catch (Exception e) {
            throw new ServiceException("Error on getAllRentalPoints.", e);
        }
    }

    // TODO пока в планах
    @Override
    public List<RentalPoint> getRentalPointsByDistrict(Long countryId, Long cityId, Long districtId) {
        // TODO мб в сервисе сделать
        return this.rentalPointRepository.findAllRentalPointByDistrict(countryId, cityId, districtId);
    }

    @Override
    // TODO возможно добавить кеширование
    public RentalPoint getRentalPointByCoordinates(BigDecimal latitude, BigDecimal longitude) {
        return this.rentalPointRepository.findRentalPointByCoordinates(latitude, longitude).orElseThrow(
                () -> new NotFoundException("Rental point with latitude: " + latitude + " and longitude: " + longitude + " not found.")
        );
    }
}
