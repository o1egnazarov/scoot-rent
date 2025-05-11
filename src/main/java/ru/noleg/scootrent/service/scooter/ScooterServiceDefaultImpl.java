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
    @Transactional
    public Long add(Scooter scooter) {
        logger.debug("Добавление самоката  {}.", scooter.getNumberPlate());

        this.validateScooterModel(scooter);
        this.validateRentalPoint(scooter.getRentalPoint().getId());

        Long id = this.scooterRepository.save(scooter).getId();

        logger.debug("Самокат успешно добавлен с id {}.", id);
        return id;
    }

    private void validateScooterModel(Scooter scooter) {
        if (!this.scooterModelRepository.existsById(scooter.getModel().getId())) {

            logger.error("Модель самоката с id {} не найдена.", scooter.getModel().getId());
            throw new NotFoundException("Scooter model with id " + scooter.getModel().getId() + " not found.");
        }
    }

    private void validateRentalPoint(Long rentalPointId) {
        LocationNode rentalPoint = this.locationRepository.findLocationById(rentalPointId).orElseThrow(() -> {
            logger.error("Пункт проката с id {} не найден", rentalPointId);
            return new NotFoundException("Пункт проката с id " + rentalPointId + " не найден.");
        });

        if (rentalPoint.getLocationType() != LocationType.RENTAL_POINT) {

            logger.warn("Локация с id {} не является пунктом проката.", rentalPointId);
            throw new BusinessLogicException("Location is not a rental point.");
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        logger.debug("Удаление самоката с id {}.", id);

        if (!this.scooterRepository.existsById(id)) {

            logger.warn("Самокат с id {} не найден для удаления.", id);
            throw new NotFoundException("Scooter with id " + id + " not found.");
        }

        this.scooterRepository.delete(id);
        logger.debug("Самокат с id {} успешно удалён.", id);
    }

    @Override
    public Scooter getScooter(Long id) {
        logger.debug("Получение самоката с id {}.", id);

        Scooter scooter = this.scooterRepository.findById(id).orElseThrow(() -> {
            logger.warn("Самокат с id {} не найден.", id);
            return new NotFoundException("Самокат с id " + id + " не найден.");
        });

        logger.debug("Самокат с id {} успешно получен.", id);
        return scooter;
    }

    @Override
    public List<Scooter> getAllScooters() {
        logger.debug("Получение всех самокатов");

        List<Scooter> scooters = this.scooterRepository.findAll();

        logger.debug("Получено {} самокатов", scooters.size());
        return scooters;
    }
}
