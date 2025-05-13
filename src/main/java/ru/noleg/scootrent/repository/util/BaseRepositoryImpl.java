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
                logger.debug("Persist new entity {}.", type);
            } else {

                this.entityManager.merge(entity);
                logger.debug("Update entity {} with id: {}.", type, id);
            }

            this.entityManager.flush();

            return entity;
        } catch (Exception e) {

            logger.error("Error on save entity {} with id: {}.", type, id, e);
            throw new RepositoryException("Repository error on save entity.", e);
        }
    }

    @Override
    public void delete(ID id) {

        final String type = getEntityClass().getSimpleName();
        try {
            logger.debug("Attempt delete {} entity with id: {}.", type, id);

            T entity = this.entityManager.find(getEntityClass(), id);
            if (entity == null) {

                logger.warn("Entity {} with id: {} not found for delete.", type, id);
                throw new RepositoryException("Entity not found");
            }

            this.entityManager.remove(entity);
            logger.debug("Entity {} with id: {} successfully delete.", type, id);
        } catch (Exception e) {

            logger.error("Error on delete entity {} with id: {}.", type, id, e);
            throw new RepositoryException("Repository error on remove entity.", e);
        }
    }

    @Override
    public List<T> findAll() {

        final String type = getEntityClass().getSimpleName();
        try {
            logger.debug("Fetch all entities {}.", type);

            final String ql = "SELECT e FROM " + type + " e";
            List<T> entities = this.entityManager.createQuery(ql, getEntityClass()).getResultList();

            logger.debug("Found {} {} entities.", entities.size(), type);
            return entities;
        } catch (Exception e) {

            logger.error("Error on fetch list entities {}.", type, e);
            throw new RepositoryException("Repository error on fetch all entities.", e);
        }
    }

    @Override
    public Optional<T> findById(ID id) {

        final String type = getEntityClass().getSimpleName();
        try {
            logger.debug("Fetch entity {} with id: {}.", type, id);

            T entity = entityManager.find(getEntityClass(), id);
            if (entity != null) {

                logger.debug("Found entity {} with id: {}.", type, id);
            } else {

                logger.warn("Entity {} with id: {} not found.", type, id);
            }
            return Optional.ofNullable(entity);
        } catch (Exception e) {

            logger.error("Error on fetch entity {} with id: {}.", type, id, e);
            throw new RepositoryException("Repository error on find entity by id.", e);
        }
    }

    @Override
    public boolean existsById(ID id) {

        final String type = getEntityClass().getSimpleName();
        try {
            boolean exists = entityManager.find(getEntityClass(), id) != null;
            logger.debug("{} with id: {} {}exist.", type, id, exists ? "" : "not ");

            return exists;
        } catch (Exception e) {

            logger.error("Error on check existence entity {} with id: {}.", type, id, e);
            throw new RepositoryException("Repository error on check existence.", e);
        }
    }
}

