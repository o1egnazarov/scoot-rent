package ru.noleg.scootrent.mapper;

import java.util.List;

public interface BaseMapper<E, D> {
    E mapToEntity(D d);

    D mapToDetailDto(E e);

    List<E> mapToEntities(List<D> dtos);

    List<D> mapToDetailDtos(List<E> entities);
}
