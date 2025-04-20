package ru.noleg.scootrent.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.noleg.scootrent.dto.rentalPoint.RentalPointDto;
import ru.noleg.scootrent.dto.rentalPoint.UpdateRentalPointDto;
import ru.noleg.scootrent.entity.rental.RentalPoint;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ScooterMapper.class})
public interface RentalPointMapper extends BaseMapper<RentalPoint, RentalPointDto> {
    @Override
    RentalPoint mapToEntity(RentalPointDto rentalPointDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRentalPointFromDto(UpdateRentalPointDto dto, @MappingTarget RentalPoint entity);

    @Override
    @Mapping(target = "totalCount", expression = "java(scooters.size())")
    RentalPointDto mapToDto(RentalPoint rentalPoint);


    @Override
    List<RentalPoint> mapToEntities(List<RentalPointDto> dtos);

    @Override
    List<RentalPointDto> mapToDtos(List<RentalPoint> entities);
}
