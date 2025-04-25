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

            if (rentalPoint.getId().equals(rentalPoint.getParent().getId())) {
                throw new ServiceException("Rental point id equals parent id.");
            }

            return this.rentalPointRepository.save(rentalPoint).getId();
        } catch (Exception e) {

            throw new ServiceException("Error on add rental point.", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {

            // TODO ничего не делаем, правильно ли так
            if (!this.rentalPointRepository.existsById(id)) {
                return;
            }

            this.rentalPointRepository.delete(id);
        } catch (Exception e) {

            throw new ServiceException("Error on delete rental point.", e);
        }
    }

    @Override
    public RentalPoint getRentalPoint(Long id) {
        try {

            return this.rentalPointRepository.findById(id).orElseThrow(
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

            return this.rentalPointRepository.findAll();
        } catch (Exception e) {

            throw new ServiceException("Error on getAllRentalPoints.", e);
        }
    }

    @Override
    public RentalPoint getRentalPointByCoordinates(BigDecimal latitude, BigDecimal longitude) {
        // TODO кидать ли тут исключение
        // TODO сравнивать через compareTo
        return this.rentalPointRepository.findAll()
                .stream()
                .filter(rp -> rp.getLatitude().compareTo(latitude) == 0 && rp.getLongitude().compareTo(longitude) == 0)
                .findFirst()
                .orElseThrow(
                        () -> new NotFoundException("No rental point at the coordinates: " + latitude + ", " + longitude)
                );
    }
}
