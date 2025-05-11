package ru.noleg.scootrent.repository.impl;

import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;
import ru.noleg.scootrent.exception.RepositoryException;
import ru.noleg.scootrent.repository.TariffRepository;
import ru.noleg.scootrent.repository.util.BaseRepositoryImpl;

import java.util.List;
import java.util.Optional;

@Repository
public class TariffRepositoryJpaImpl extends BaseRepositoryImpl<Tariff, Long> implements TariffRepository {
    @Override
    protected Class<Tariff> getEntityClass() {
        return Tariff.class;
    }

    @Override
    protected Long getEntityId(Tariff entity) {
        return entity.getId();
    }

    @Override
    public Optional<Tariff> findDefaultTariff() {
        try {

            final String ql = "SELECT t FROM Tariff t WHERE t.type = :type";

            TypedQuery<Tariff> query = entityManager.createQuery(ql, Tariff.class);
            query.setParameter("type", TariffType.DEFAULT_TARIFF);

            List<Tariff> resultList = query.getResultList();
            return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
        } catch (Exception e) {
            throw new RepositoryException("Repository error on fetch default tariff.", e);
        }
    }
}
