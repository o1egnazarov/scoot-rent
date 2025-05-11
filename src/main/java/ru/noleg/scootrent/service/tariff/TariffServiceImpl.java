package ru.noleg.scootrent.service.tariff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.TariffRepository;

import java.util.List;

@Service
public class TariffServiceImpl implements TariffService {

    private static final Logger logger = LoggerFactory.getLogger(TariffServiceImpl.class);

    private final TariffRepository tariffRepository;

    public TariffServiceImpl(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    @Transactional
    public Long createTariff(Tariff tariff) {
        logger.debug("Создание нового тарифа: {}", tariff.getTitle());

        Long id = tariffRepository.save(tariff).getId();
        logger.debug("Тариф создан с ID: {}", id);
        return id;
    }

    @Override
    public Tariff getTariff(Long id) {
        logger.debug("Получение тарифа по id: {}.", id);
        return this.tariffRepository.findById(id).orElseThrow(
                () -> {
                    logger.error("Тариф с id: {} не найден.", id);
                    return new NotFoundException("Tariff with id " + id + " not found");
                }
        );
    }

    @Override
    public List<Tariff> getAllTariffs() {
        logger.debug("Получение списка всех тарифов.");

        List<Tariff> tariffs = this.tariffRepository.findAll();
        logger.debug("Найдено {} тарифов", tariffs.size());
        return tariffs;
    }

    @Override
    public List<Tariff> getActiveTariffs() {
        logger.debug("Получение списка активных тарифов.");

        List<Tariff> activeTariffs = this.tariffRepository.findByActiveTrue();
        logger.debug("Найдено {} активных тарифов.", activeTariffs.size());
        return activeTariffs;
    }

    @Override
    @Transactional
    public void deactivateTariff(Long id) {
        logger.debug("Деактивация тарифа с id: {}.", id);

        Tariff tariff = this.getTariff(id);
        tariff.deactivateTariff();
        this.tariffRepository.save(tariff);
        logger.debug("Тариф с id {} успешно деактивирован.", id);
    }
}
