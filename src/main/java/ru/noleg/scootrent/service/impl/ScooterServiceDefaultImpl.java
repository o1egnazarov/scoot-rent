package ru.noleg.scootrent.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.repository.ScooterModelRepository;
import ru.noleg.scootrent.repository.ScooterRepository;
import ru.noleg.scootrent.service.ScooterService;

import java.util.List;

@Service
public class ScooterServiceDefaultImpl implements ScooterService {

    private final ScooterRepository scooterRepository;
    private final ScooterModelRepository scooterModelRepository;

    public ScooterServiceDefaultImpl(ScooterRepository scooterRepository, ScooterModelRepository scooterModelRepository) {
        this.scooterRepository = scooterRepository;
        this.scooterModelRepository = scooterModelRepository;
    }

    @Override
    @Transactional
    public Long add(Scooter scooter) {
        try {

            if (!this.scooterModelRepository.existsById(scooter.getModel().getId())) {

                throw new NotFoundException("Scooter model with id " + scooter.getModel().getId() + " not found");
            }

            return this.scooterRepository.save(scooter).getId();
        } catch (NotFoundException e) {

            throw e;
        } catch (Exception e) {

            throw new ServiceException("Error on add scooter.", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {

            // TODO ничего не делаем, правильно ли так
            if (!this.scooterRepository.existsById(id)) {
                return;
            }

            this.scooterRepository.delete(id);
        } catch (Exception e) {

            throw new ServiceException("Error on delete scooter.", e);
        }
    }

    @Override
    public Scooter getScooter(Long id) {
        try {

            return this.scooterRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Scooter with id " + id + " not found")
            );
        } catch (NotFoundException e) {

            throw e;
        } catch (Exception e) {

            throw new ServiceException("Error on get scooter.", e);
        }
    }

    @Override
    public List<Scooter> getAllScooters() {
        try {

            return this.scooterRepository.findAll();
        } catch (Exception e) {

            throw new ServiceException("Error on get all scooters.", e);
        }
    }
}
