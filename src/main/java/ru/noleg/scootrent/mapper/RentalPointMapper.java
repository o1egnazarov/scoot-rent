package ru.noleg.scootrent.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.noleg.scootrent.dto.rentalPoint.CreateRentalPointDto;
import ru.noleg.scootrent.dto.rentalPoint.DetailRentalPointDto;
import ru.noleg.scootrent.dto.rentalPoint.RentalPointDto;
import ru.noleg.scootrent.dto.rentalPoint.ShortRentalPointDto;
import ru.noleg.scootrent.dto.rentalPoint.UpdateRentalPointDto;
import ru.noleg.scootrent.entity.rental.RentalPoint;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ScooterMapper.class})
public interface RentalPointMapper extends BaseMapper<RentalPoint, RentalPointDto> {
    RentalPoint mapToEntity(DetailRentalPointDto detailRentalPointDto);

    @Mapping(target = "parent.id", source = "parentId")
    RentalPoint mapToEntity(CreateRentalPointDto createRentalPointDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRentalPointFromDto(UpdateRentalPointDto dto, @MappingTarget RentalPoint entity);

    @Mapping(target = "totalCount", expression = "java(scooters.size())")
    DetailRentalPointDto mapToDetailDto(RentalPoint rentalPoint);

    RentalPointDto mapToDto(RentalPoint rentalPoint);

    List<RentalPointDto> mapToDtos(List<RentalPoint> rentalPoints);

    ShortRentalPointDto mapToShortDto(RentalPoint rentalPoint);

    List<DetailRentalPointDto> mapToDetailDtos(List<RentalPoint> entities);
}
