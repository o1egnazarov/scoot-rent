package ru.noleg.scootrent.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.noleg.scootrent.dto.scooter.ScooterDto;
import ru.noleg.scootrent.dto.scooter.ScooterDtoWithModel;
import ru.noleg.scootrent.dto.scooter.UpdateScooterDto;
import ru.noleg.scootrent.entity.scooter.Scooter;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ScooterMapper extends BaseMapper<Scooter, ScooterDto> {
    @Override
    @Mapping(target = "model.id", source = "modelId")
    Scooter mapToEntity(ScooterDto scooterDto);

    ScooterDtoWithModel mapToDtoWithModel(Scooter scooter);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateScooterFromDto(UpdateScooterDto dto, @MappingTarget Scooter entity);

    @Override
    @Mapping(target = "modelId", source = "model.id")
    ScooterDto mapToDto(Scooter scooter);

    @Override
    List<Scooter> mapToEntities(List<ScooterDto> dtos);

    @Override
    List<ScooterDto> mapToDtos(List<Scooter> entities);
}
