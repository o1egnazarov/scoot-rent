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

        ID id = getEntityId(entity);
        try {

            if (id == null) {

                this.entityManager.persist(entity);
                logger.info("Persisted new {} entity successfully.", getEntityClass().getSimpleName());
            } else {

                this.entityManager.merge(entity);
                logger.info("Updated {} entity successfully with id: {}.", getEntityClass().getSimpleName(), id);
            }

            this.entityManager.flush();

            return entity;
        } catch (Exception e) {

            logger.error("Failed to save {} entity with id: {}.", getEntityClass().getSimpleName(), id, e);
            throw new RepositoryException("Repository error on save entity.", e);
        }
    }

    @Override
    public void delete(ID id) {

        final String type = getEntityClass().getSimpleName();
        try {
            logger.debug("Attempting to delete {} entity with id: {}...", type, id);

            T entity = this.entityManager.find(getEntityClass(), id);
            if (entity == null) {

                logger.warn("Entity of type {} not found for deletion with id: {}.", type, id);
                throw new RepositoryException("Entity not found");
            }

            this.entityManager.remove(entity);
            logger.info("Entity {} delete successfully with id: {}.", type, id);
        } catch (Exception e) {

            logger.error("Failed to delete entity {}.", type, e);
            throw new RepositoryException("Repository error on remove entity.", e);
        }
    }

    @Override
    public List<T> findAll() {

        final String type = getEntityClass().getSimpleName();
        try {
            logger.debug("Fetching all {} entities...", type);

            final String ql = "SELECT e FROM " + type + " e";
            List<T> entities = this.entityManager.createQuery(ql, getEntityClass()).getResultList();

            logger.info("Fetched {} {} entities.", entities.size(), type);
            return entities;
        } catch (Exception e) {

            logger.error("Failed to fetch entities {}.", type, e);
            throw new RepositoryException("Repository error on fetch all entities.", e);
        }
    }

    @Override
    public Optional<T> findById(ID id) {

        final String type = getEntityClass().getSimpleName();
        try {
            logger.debug("Fetching {} entity by id: {}.", type, id);

            T entity = entityManager.find(getEntityClass(), id);
            if (entity != null) {

                logger.info("Found {} entity with id: {}.", type, id);
            } else {

                logger.warn("No {} entity found with id: {}.", type, id);
            }
            return Optional.ofNullable(entity);
        } catch (Exception e) {

            logger.error("Failed to fetch entity {} by id: {}.", type, id, e);
            throw new RepositoryException("Repository error on find entity by id.", e);
        }
    }

    @Override
    public boolean existsById(ID id) {

        final String type = getEntityClass().getSimpleName();
        try {
            boolean exists = entityManager.find(getEntityClass(), id) != null;
            logger.debug("{} entity existence check for id: {}. Exists: {}.", type, id, exists);

            return exists;
        } catch (Exception e) {

            logger.error("Failed to check existence of {} entity with id: {}.", type, id, e);
            throw new RepositoryException("Repository error on check existence.", e);
        }
    }
}

