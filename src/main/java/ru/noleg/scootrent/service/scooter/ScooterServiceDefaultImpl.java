package ru.noleg.scootrent.service.scooter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.location.LocationType;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.LocationRepository;
import ru.noleg.scootrent.repository.ScooterModelRepository;
import ru.noleg.scootrent.repository.ScooterRepository;

import java.util.List;

@Service
@Transactional
public class ScooterServiceDefaultImpl implements ScooterService {

    private static final Logger logger = LoggerFactory.getLogger(ScooterServiceDefaultImpl.class);

    private final ScooterRepository scooterRepository;
    private final ScooterModelRepository scooterModelRepository;
    private final LocationRepository locationRepository;

    public ScooterServiceDefaultImpl(ScooterRepository scooterRepository,
                                     ScooterModelRepository scooterModelRepository,
                                     LocationRepository locationRepository) {
        this.scooterRepository = scooterRepository;
        this.scooterModelRepository = scooterModelRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public Long add(Scooter scooter) {
        logger.debug("Adding scooter with number plate: {}.", scooter.getNumberPlate());

        this.validateScooterModel(scooter);
        this.validateRentalPoint(scooter.getRentalPoint().getId());

        Long id = this.scooterRepository.save(scooter).getId();

        logger.debug("Scooter successfully added with id: {}.", id);
        return id;
    }

    private void validateScooterModel(Scooter scooter) {
        if (!this.scooterModelRepository.existsById(scooter.getModel().getId())) {

            logger.error("Scooter model with id: {} not found.", scooter.getModel().getId());
            throw new NotFoundException("Scooter model with id " + scooter.getModel().getId() + " not found.");
        }
    }

    private void validateRentalPoint(Long rentalPointId) {
        LocationNode rentalPoint = this.locationRepository.findLocationById(rentalPointId).orElseThrow(() -> {
            logger.error("Location with id: {} not found.", rentalPointId);
            return new NotFoundException("Location with id " + rentalPointId + " not found.");
        });

        if (rentalPoint.getLocationType() != LocationType.RENTAL_POINT) {

            logger.warn("Location with id:{} is not a rental point.", rentalPointId);
            throw new BusinessLogicException("Location is not a rental point.");
        }
    }

    @Override
    public void delete(Long id) {
        logger.debug("Deleting scooter with id: {}.", id);

        if (!this.scooterRepository.existsById(id)) {

            logger.warn("Scooter with id: {} not found.", id);
            throw new NotFoundException("Scooter with id " + id + " not found.");
        }

        this.scooterRepository.delete(id);
        logger.debug("Scooter with id: {} successfully deleted.", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Scooter getScooter(Long id) {
        logger.debug("Fetching scooter with id: {}.", id);

        return this.scooterRepository.findById(id).orElseThrow(() -> {
            logger.error("Scooter with id: {} not found.", id);
            return new NotFoundException("Scooter with id " + id + " not found.");
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scooter> getAllScooters() {
        logger.debug("Fetching all scooters.");

        List<Scooter> scooters = this.scooterRepository.findAll();

        logger.debug("Got {} scooters.", scooters.size());
        return scooters;
    }
}
