package ru.noleg.scootrent.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.location.LocationType;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.repository.LocationRepository;
import ru.noleg.scootrent.repository.ScooterModelRepository;
import ru.noleg.scootrent.repository.ScooterRepository;
import ru.noleg.scootrent.service.ScooterService;

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
        try {
            logger.info("Добавление самоката  {}.", scooter.getNumberPlate());

            if (!this.scooterModelRepository.existsById(scooter.getModel().getId())) {

                logger.warn("Модель самоката с id {} не найдена.", scooter.getModel().getId());
                throw new NotFoundException("Scooter model with id " + scooter.getModel().getId() + " not found.");
            }

            Long rentalPointId = scooter.getRentalPoint().getId();

            LocationNode rentalPoint = this.locationRepository.findLocationById(rentalPointId).orElseThrow(() -> {
                logger.warn("Пункт проката с id {} не найден", rentalPointId);
                return new NotFoundException("Пункт проката с id " + rentalPointId + " не найден.");
            });

            if (rentalPoint.getLocationType() != LocationType.RENTAL_POINT) {

                logger.warn("Локация с id {} не является пунктом проката.", rentalPointId);
                throw new BusinessLogicException("Location is not a rental point.");
            }

            Long id = this.scooterRepository.save(scooter).getId();
            logger.info("Самокат успешно добавлен с id {}.", id);

            return id;
        } catch (NotFoundException | BusinessLogicException e) {

            logger.error("Ошибка бизнес-логики при добавлении самоката: {}.", e.getMessage());
            throw e;
        } catch (Exception e) {

            logger.error("Неожиданная ошибка при добавлении самоката.", e);
            throw new ServiceException("Error on add scooter.", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            logger.info("Удаление самоката с id {}.", id);

            if (!this.scooterRepository.existsById(id)) {

                logger.warn("Самокат с id {} не найден для удаления.", id);
                throw new NotFoundException("Scooter with id " + id + " not found.");
            }

            this.scooterRepository.delete(id);

            logger.info("Самокат с id {} успешно удалён.", id);
        } catch (NotFoundException e) {

            logger.warn("Ошибка при удалении самоката: {}.", e.getMessage());
            throw e;
        } catch (Exception e) {

            logger.error("Неожиданная ошибка при удалении самоката с id {}.", id, e);
            throw new ServiceException("Error on delete scooter.", e);
        }
    }

    @Override
    public Scooter getScooter(Long id) {
        try {
            logger.info("Получение самоката с id {}.", id);

            Scooter scooter = this.scooterRepository.findById(id).orElseThrow(() -> {
                logger.warn("Самокат с id {} не найден.", id);
                return new NotFoundException("Самокат с id " + id + " не найден.");
            });

            logger.info("Самокат с id {} успешно получен.", id);
            return scooter;
        } catch (NotFoundException e) {

            logger.warn("Ошибка при получении самоката: {}.", e.getMessage());
            throw e;
        } catch (Exception e) {

            logger.error("Неожиданная ошибка при получении самоката с id {}.", id, e);
            throw new ServiceException("Error on get scooter.", e);
        }
    }

    @Override
    public List<Scooter> getAllScooters() {
        try {
            logger.info("Получение всех самокатов");

            List<Scooter> scooters = this.scooterRepository.findAll();

            logger.info("Получено {} самокатов", scooters.size());
            return scooters;
        } catch (Exception e) {

            logger.error("Неожиданная ошибка при получении всех самокатов", e);
            throw new ServiceException("Error on get all scooters.", e);
        }
    }
}
