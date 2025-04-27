package ru.noleg.scootrent.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.noleg.scootrent.dto.rentalPoint.CreateRentalPointDto;
import ru.noleg.scootrent.dto.rentalPoint.DetailRentalPointDto;
import ru.noleg.scootrent.dto.rentalPoint.ShortRentalPointDto;
import ru.noleg.scootrent.dto.rentalPoint.UpdateRentalPointDto;
import ru.noleg.scootrent.entity.rental.RentalPoint;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ScooterMapper.class})
public interface RentalPointMapper extends BaseMapper<RentalPoint, DetailRentalPointDto> {
    @Override
    RentalPoint mapToEntity(DetailRentalPointDto detailRentalPointDto);

    @Mapping(target = "parent.id", source = "parentId")
    RentalPoint mapToEntity(CreateRentalPointDto createRentalPointDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRentalPointFromDto(UpdateRentalPointDto dto, @MappingTarget RentalPoint entity);

    @Override
    @Mapping(target = "totalCount", expression = "java(scooters.size())")
    DetailRentalPointDto mapToDto(RentalPoint rentalPoint);

    ShortRentalPointDto mapToShortDto(RentalPoint rentalPoint);

    @Override
    List<RentalPoint> mapToEntities(List<DetailRentalPointDto> dtos);

    @Override
    List<DetailRentalPointDto> mapToDtos(List<RentalPoint> entities);
}
