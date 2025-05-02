package ru.noleg.scootrent.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.noleg.scootrent.dto.scooter.scootermodel.ScooterModelDto;
import ru.noleg.scootrent.dto.scooter.scootermodel.UpdateScooterModelDto;
import ru.noleg.scootrent.entity.scooter.ScooterModel;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ScooterModelMapper extends BaseMapper<ScooterModel, ScooterModelDto> {
    @Override
    ScooterModel mapToEntity(ScooterModelDto scooterModelDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateScooterModelFromDto(UpdateScooterModelDto dto, @MappingTarget ScooterModel entity);

    @Override
    ScooterModelDto mapToDto(ScooterModel scooterModel);

    @Override
    List<ScooterModel> mapToEntities(List<ScooterModelDto> dtos);

    @Override
    List<ScooterModelDto> mapToDtos(List<ScooterModel> entities);
}