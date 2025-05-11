package ru.noleg.scootrent.service.tariff;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.exception.NotFoundException;
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
        return this.tariffRepository.save(tariff).getId();
    }

    @Override
    public Tariff getTariff(Long id) {
        return this.tariffRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Tariff with id " + id + " not found")
        );
    }

    @Override
    public List<Tariff> getAllTariffs() {
        return this.tariffRepository.findAll();
    }

    @Override
    public List<Tariff> getActiveTariffs() {
        List<Tariff> tariffs = this.tariffRepository.findAll();
        return tariffs.stream()
                .filter(Tariff::getActive)
                .toList();
    }

    @Override
    @Transactional
    public void deactivateTariff(Long id) {
        Tariff tariff = this.getTariff(id);
        tariff.deactivateTariff();
        this.tariffRepository.save(tariff);
    }
}
