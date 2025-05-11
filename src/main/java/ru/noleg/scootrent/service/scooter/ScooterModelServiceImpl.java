package ru.noleg.scootrent.service.scooter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.scooter.ScooterModel;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.ScooterModelRepository;

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
        logger.debug("Добавление новой модели самоката {}.", scooterModel.getTitle());

        Long id = this.scooterModelRepository.save(scooterModel).getId();

        logger.debug("Модель с id: {} успешно добавлена.", id);
        return id;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        logger.debug("Удаление модели самоката с id {}.", id);

        if (!this.scooterModelRepository.existsById(id)) {
            logger.warn("Модель самоката с id {} не найдена для удаления.", id);
            throw new NotFoundException("Scooter model with id " + id + " not found.");
        }

        this.scooterModelRepository.delete(id);
        logger.debug("Модель самоката с id {} успешно удалена.", id);
    }

    @Override
    public ScooterModel getScooterModel(Long id) {
        logger.debug("Получение модели самоката с id {}.", id);

        ScooterModel model = this.scooterModelRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Модель самоката с id " + id + " не найдена.")
        );

        logger.debug("Модель самоката с id {} успешно получена.", id);
        return model;
    }

    @Override
    public List<ScooterModel> getAllScooterModels() {
        logger.debug("Получение всех моделей самокатов.");

        List<ScooterModel> models = this.scooterModelRepository.findAll();

        logger.debug("Получено {} моделей самокатов.", models.size());
        return models;
    }
}
