package ru.noleg.scootrent.repository.util;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, ID> {

    T save(T entity);

    void delete(ID id);

    Optional<T> findById(ID id);

    List<T> findAll();

    boolean existsById(ID id);

    T getReference(ID id);
}
