package ru.noleg.scootrent.service.tariff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.TariffRepository;

import java.util.List;

@Service
@Transactional
public class TariffServiceImpl implements TariffService {

    private static final Logger logger = LoggerFactory.getLogger(TariffServiceImpl.class);

    private final TariffRepository tariffRepository;

    public TariffServiceImpl(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    @Override
    public Long createTariff(Tariff tariff) {
        logger.debug("Creating tariff: {}.", tariff.getTitle());

        Long id = tariffRepository.save(tariff).getId();
        logger.debug("Tariff created with id: {}.", id);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public Tariff getTariff(Long id) {
        logger.debug("Fetching tariff by id: {}.", id);
        return this.tariffRepository.findById(id).orElseThrow(
                () -> {
                    logger.error("Tariff with id: {} not found.", id);
                    return new NotFoundException("Tariff with id " + id + " not found");
                }
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tariff> getAllTariffs() {
        logger.debug("Fetching list tariffs.");
        return this.tariffRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tariff> getActiveTariffs() {
        logger.debug("Fetching list active tariffs.");

        List<Tariff> activeTariffs = this.tariffRepository.findByActiveTrue();
        logger.debug("Got {} active tariffs.", activeTariffs.size());
        return activeTariffs;
    }

    @Override
    public void deactivateTariff(Long id) {
        logger.debug("Deactivating tariff with id: {}.", id);

        Tariff tariff = this.getTariff(id);
        if (!tariff.getIsActive()) {
            logger.warn("Tariff already deactivated.");
            throw new BusinessLogicException("Tariff with id: " + id + " already deactivated.");
        }
        tariff.deactivateTariff();
        this.tariffRepository.save(tariff);

        logger.debug("Tariff with id {} successfully deactivated.", id);
    }

    @Override
    public void activateTariff(Long id) {
        logger.debug("Activating tariff with id: {}.", id);

        Tariff tariff = this.getTariff(id);
        if (tariff.getIsActive()) {
            logger.warn("Tariff already activated.");
            throw new BusinessLogicException("Tariff with id: " + id + " already activated.");
        }
        tariff.activateTariff();
        this.tariffRepository.save(tariff);

        logger.debug("Tariff with id {} successfully activated.", id);
    }
}
