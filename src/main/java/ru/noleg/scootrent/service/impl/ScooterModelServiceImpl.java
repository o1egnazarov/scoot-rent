package ru.noleg.scootrent.service.impl;

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

    private final ScooterModelRepository scooterModelRepository;

    public ScooterModelServiceImpl(ScooterModelRepository scooterModelRepository) {
        this.scooterModelRepository = scooterModelRepository;
    }

    @Override
    @Transactional
    public Long add(ScooterModel scooterModel) {
        try {

            return this.scooterModelRepository.save(scooterModel).getId();
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error on add scooter model.", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {

            if (!this.scooterModelRepository.existsById(id)) {
                throw new NotFoundException("Scooter model with id " + id + " not found.");
            }
            this.scooterModelRepository.delete(id);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error on delete scooter model.", e);
        }
    }

    @Override
    public ScooterModel getScooterModel(Long id) {
        try {

            return this.scooterModelRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Scooter model with id " + id + " not found.")
            );
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error on get scooter model.", e);
        }
    }

    @Override
    public List<ScooterModel> getAllScooterModels() {
        try {

            return this.scooterModelRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Error on get all scooter models.", e);
        }
    }
}
