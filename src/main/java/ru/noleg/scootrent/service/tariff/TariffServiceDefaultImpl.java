package ru.noleg.scootrent.service.tariff;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.repository.TariffRepository;

import java.util.List;

@Service
public class TariffServiceDefaultImpl implements TariffService {

    private final TariffRepository tariffRepository;

    public TariffServiceDefaultImpl(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    @Transactional
    public Long createTariff(Tariff tariff) {
        try {

            return this.tariffRepository.save(tariff).getId();
        } catch (Exception e) {
            throw new ServiceException("Error on create tariff", e);
        }
    }

    @Override
    public Tariff getTariff(Long id) {
        try {

            return this.tariffRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Tariff with id " + id + " not found")
            );
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error on get tariff", e);
        }
    }

    @Override
    public List<Tariff> getAllTariffs() {
        try {

            return this.tariffRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Error on get tariffs", e);
        }
    }

    @Override
    public List<Tariff> getActiveTariffs() {
        try {

            List<Tariff> tariffs = this.tariffRepository.findAll();
            return tariffs.stream()
                    .filter(Tariff::getActive)
                    .toList();
        } catch (Exception e) {
            throw new ServiceException("Error on get tariffs", e);
        }
    }

    @Override
    @Transactional
    public void deactivateTariff(Long id) {
        try {

            Tariff tariff = this.getTariff(id);
            tariff.deactivateTariff();
            this.tariffRepository.save(tariff);
        } catch (Exception e) {
            throw new ServiceException("Error on deactivate tariff", e);
        }
    }
}
