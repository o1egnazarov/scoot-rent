package ru.noleg.scootrent.repository.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.noleg.scootrent.exception.RepositoryException;

import java.util.List;
import java.util.Optional;

public abstract class BaseRepositoryImpl<T, ID> implements BaseRepository<T, ID> {

    private static final Logger logger = LoggerFactory.getLogger(BaseRepositoryImpl.class.getName());

    @PersistenceContext
    protected EntityManager entityManager;

    protected abstract Class<T> getEntityClass();

    protected abstract ID getEntityId(T entity);

    @Override
    public T save(T entity) {

        final String type = getEntityClass().getSimpleName();
        final ID id = getEntityId(entity);
        try {

            if (id == null) {

                this.entityManager.persist(entity);
                logger.debug("Создана новая сущность {}.", type);
            } else {

                this.entityManager.merge(entity);
                logger.debug("Обновлена сущность {} с id: {}.", type, id);
            }

            this.entityManager.flush();

            return entity;
        } catch (Exception e) {

            logger.error("Ошибка сохранения сущности {} с id: {}.", type, id, e);
            throw new RepositoryException("Repository error on save entity.", e);
        }
    }

    @Override
    public void delete(ID id) {

        final String type = getEntityClass().getSimpleName();
        try {
            logger.debug("Попытка удаления {} сущности с id: {}.", type, id);

            T entity = this.entityManager.find(getEntityClass(), id);
            if (entity == null) {

                logger.warn("Сущность {} не найдена для удаления по id: {}.", type, id);
                throw new RepositoryException("Entity not found");
            }

            this.entityManager.remove(entity);
            logger.debug("Сущность {} c id: {} удалена успешно.", type, id);
        } catch (Exception e) {

            logger.error("Ошибка удаления сущности {} с id: {}.", type, id, e);
            throw new RepositoryException("Repository error on remove entity.", e);
        }
    }

    @Override
    public List<T> findAll() {

        final String type = getEntityClass().getSimpleName();
        try {
            logger.debug("Получение всех сущностей {}.", type);

            final String ql = "SELECT e FROM " + type + " e";
            List<T> entities = this.entityManager.createQuery(ql, getEntityClass()).getResultList();

            logger.debug("Найдено {} {} сущностей.", entities.size(), type);
            return entities;
        } catch (Exception e) {

            logger.error("Ошибка получения списка сущностей {}.", type, e);
            throw new RepositoryException("Repository error on fetch all entities.", e);
        }
    }

    @Override
    public Optional<T> findById(ID id) {

        final String type = getEntityClass().getSimpleName();
        try {
            logger.debug("Получение сущности {} с id: {}.", type, id);

            T entity = entityManager.find(getEntityClass(), id);
            if (entity != null) {

                logger.debug("Найдена сущность {} с id: {}.", type, id);
            } else {

                logger.warn("Сущность {} не найдена по id: {}.", type, id);
            }
            return Optional.ofNullable(entity);
        } catch (Exception e) {

            logger.error("Ошибка получения сущности {} по id: {}.", type, id, e);
            throw new RepositoryException("Repository error on find entity by id.", e);
        }
    }

    @Override
    public boolean existsById(ID id) {

        final String type = getEntityClass().getSimpleName();
        try {
            boolean exists = entityManager.find(getEntityClass(), id) != null;
            logger.debug("{} с ID: {} {}существует.", type, id, exists ? "" : "не ");

            return exists;
        } catch (Exception e) {

            logger.error("Ошибка проверки существования сущности {} с id: {}.", type, id, e);
            throw new RepositoryException("Repository error on check existence.", e);
        }
    }
}

