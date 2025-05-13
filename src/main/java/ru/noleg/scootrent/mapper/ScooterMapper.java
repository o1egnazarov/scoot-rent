package ru.noleg.scootrent.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.noleg.scootrent.dto.scooter.ScooterDto;
import ru.noleg.scootrent.dto.scooter.ScooterDtoWithModel;
import ru.noleg.scootrent.dto.scooter.ShortScooterDtoWithModel;
import ru.noleg.scootrent.dto.scooter.UpdateScooterDto;
import ru.noleg.scootrent.entity.scooter.Scooter;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ScooterMapper extends BaseMapper<Scooter, ScooterDto> {
    @Override
    @Mapping(target = "model.id", source = "modelId")
    @Mapping(target = "rentalPoint.id", source = "rentalPointId")
    Scooter mapToEntity(ScooterDto scooterDto);

    ScooterDtoWithModel mapToDtoWithModel(Scooter scooter);

    @Mapping(source = "model.title", target = "modelTitle")
    ShortScooterDtoWithModel mapToShortDtoWithModel(Scooter scooter);

    @Mapping(target = "rentalPoint.id", source = "rentalPointId")
    void updateScooterFromDto(UpdateScooterDto dto, @MappingTarget Scooter entity);

    @Override
    @Mapping(target = "modelId", source = "model.id")
    @Mapping(target = "rentalPointId", source = "rentalPoint.id")
    ScooterDto mapToDto(Scooter scooter);

    @Override
    List<Scooter> mapToEntities(List<ScooterDto> dtos);

    @Override
    List<ScooterDto> mapToDtos(List<Scooter> scooters);
}
