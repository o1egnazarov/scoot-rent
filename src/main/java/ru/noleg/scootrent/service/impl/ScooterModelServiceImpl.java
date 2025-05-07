package ru.noleg.scootrent.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.scooter.ScooterModel;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.repository.ScooterModelRepository;
import ru.noleg.scootrent.service.ScooterModelService;

import java.util.List;

@Service
public class ScooterModelServiceImpl implements ScooterModelService {

    private static final Logger logger = LoggerFactory.getLogger(ScooterModelServiceImpl.class);

    private final ScooterModelRepository scooterModelRepository;

    public ScooterModelServiceImpl(ScooterModelRepository scooterModelRepository) {
        this.scooterModelRepository = scooterModelRepository;
    }

    @Override
    @Transactional
    public Long add(ScooterModel scooterModel) {
        try {
            logger.info("Добавление новой модели самоката {}.", scooterModel.getTitle());
            Long id = this.scooterModelRepository.save(scooterModel).getId();

            logger.info("Модель с id: {} успешно добавлена.", id);
            return id;
        } catch (Exception e) {

            logger.error("Ошибка при добавлении модели самоката {}.", scooterModel.getTitle(), e);
            throw new ServiceException("Error on add scooter model.", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            logger.info("Удаление модели самоката с id {}.", id);

            if (!this.scooterModelRepository.existsById(id)) {

                logger.warn("Модель самоката с id {} не найдена для удаления.", id);
                throw new NotFoundException("Scooter model with id " + id + " not found.");
            }

            this.scooterModelRepository.delete(id);
            logger.info("Модель самоката с id {} успешно удалена.", id);
        } catch (NotFoundException e) {

            logger.error("Ошибка при удалении модели самоката c id {}.", id, e);
            throw e;
        } catch (Exception e) {

            logger.error("Неожиданная ошибка при удалении модели самоката с id {}.", id, e);
            throw new ServiceException("Error on delete scooter model.", e);
        }
    }

    @Override
    public ScooterModel getScooterModel(Long id) {
        try {
            logger.info("Получение модели самоката с id {}.", id);

            ScooterModel model = this.scooterModelRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Модель самоката с id " + id + " не найдена.")
            );

            logger.info("Модель самоката с id {} успешно получена.", id);
            return model;
        } catch (NotFoundException e) {

            logger.error("Модель самоката c id {} не найдена.", id, e);
            throw e;
        } catch (Exception e) {

            logger.error("Неожиданная ошибка при получении модели самоката с id {}.", id, e);
            throw new ServiceException("Error on get scooter model.", e);
        }
    }

    @Override
    public List<ScooterModel> getAllScooterModels() {
        try {
            logger.info("Получение всех моделей самокатов.");

            List<ScooterModel> models = this.scooterModelRepository.findAll();

            logger.info("Получено {} моделей самокатов.", models.size());
            return models;
        } catch (Exception e) {

            logger.error("Неожиданная ошибка при получении всех моделей самокатов.", e);
            throw new ServiceException("Error on get all scooter models.", e);
        }
    }
}
