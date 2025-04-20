package ru.noleg.scootrent.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import ru.noleg.scootrent.exception.RepositoryException;

import java.util.List;
import java.util.Optional;

public abstract class BaseRepositoryImpl<T, ID> implements BaseRepository<T, ID> {

    @PersistenceContext(unitName = "scoot-rent-unit")
    protected EntityManager entityManager;

    protected abstract Class<T> getEntityClass();

    protected abstract ID getEntityId(T entity);

    @Override
    public T save(T entity) {
        try {

            if (getEntityId(entity) == null) {

                this.entityManager.persist(entity);
            } else {

                this.entityManager.merge(entity);
            }

            this.entityManager.flush();
            return entity;
        } catch (Exception e) {

            throw new RepositoryException("Repository error on save entity", e);
        }
    }

    @Override
    public void delete(ID id) {
        try {

            T entity = this.entityManager.find(getEntityClass(), id);
            if (entity == null) {

                throw new RepositoryException("Entity not found");
            }

            this.entityManager.remove(entity);
        } catch (Exception e) {

            throw new RepositoryException("Repository error on remove entity", e);
        }
    }

    @Override
    public List<T> findAll() {

        final String ql = "SELECT e FROM " + getEntityClass().getSimpleName() + " e";
        try {

            TypedQuery<T> query = this.entityManager.createQuery(ql, getEntityClass());
            return query.getResultList();
        } catch (Exception e) {

            throw new RepositoryException("Repository error on fetch all entities", e);
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        try {

            return Optional.ofNullable(this.entityManager.find(getEntityClass(), id));
        } catch (Exception e) {

            throw new RepositoryException("Repository error on find entity by id", e);
        }
    }

    @Override
    public boolean existsById(ID id) {
        try {

            return this.entityManager.find(getEntityClass(), id) != null;
        } catch (Exception e) {

            throw new RepositoryException("Repository error on check existence", e);
        }
    }
}

