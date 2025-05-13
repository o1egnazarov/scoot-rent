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
@Transactional
public class ScooterModelServiceImpl implements ScooterModelService {

    private static final Logger logger = LoggerFactory.getLogger(ScooterModelServiceImpl.class);

    private final ScooterModelRepository scooterModelRepository;

    public ScooterModelServiceImpl(ScooterModelRepository scooterModelRepository) {
        this.scooterModelRepository = scooterModelRepository;
    }

    @Override
    public Long add(ScooterModel scooterModel) {
        logger.debug("Adding scooter model {}.", scooterModel.getTitle());

        Long id = this.scooterModelRepository.save(scooterModel).getId();

        logger.debug("Model with id: {} successfully added.", id);
        return id;
    }

    @Override
    public void delete(Long id) {
        logger.debug("Deleting scooter model with id: {}.", id);

        if (!this.scooterModelRepository.existsById(id)) {
            logger.warn("Scooter model with id: {} not found.", id);
            throw new NotFoundException("Scooter model with id " + id + " not found.");
        }

        this.scooterModelRepository.delete(id);
        logger.debug("Model with id: {} successfully deleted.", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ScooterModel getScooterModel(Long id) {
        logger.debug("Fetching scooter model by id: {}.", id);

        return this.scooterModelRepository.findById(id).orElseThrow(
                () -> {
                    logger.error("Scooter model with id: {} not found.", id);
                    return new NotFoundException("Scooter model with id: " + id + " not found.");
                }
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScooterModel> getAllScooterModels() {
        logger.debug("Fetching all scooter models.");

        List<ScooterModel> models = this.scooterModelRepository.findAll();

        logger.debug("Got {} scooter models.", models.size());
        return models;
    }
}
