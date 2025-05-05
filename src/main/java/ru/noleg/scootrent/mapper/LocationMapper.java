package ru.noleg.scootrent.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.noleg.scootrent.dto.location.CreateLocationDto;
import ru.noleg.scootrent.dto.location.DetailLocationDto;
import ru.noleg.scootrent.dto.location.LocationDto;
import ru.noleg.scootrent.dto.location.ShortLocationDto;
import ru.noleg.scootrent.dto.location.UpdateLocationDto;
import ru.noleg.scootrent.entity.location.LocationNode;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ScooterMapper.class})
public interface LocationMapper extends BaseMapper<LocationNode, LocationDto> {
    @Mapping(target = "parent.id", source = "parentId")
    LocationNode mapToEntity(CreateLocationDto createLocationDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRentalPointFromDto(UpdateLocationDto dto, @MappingTarget LocationNode entity);

    @Mapping(target = "totalCount", expression = "java(scooters.size())")
    DetailLocationDto mapToDetailDto(LocationNode locationNode);

    List<DetailLocationDto> mapToDetailDtos(List<LocationNode> entities);

    LocationDto mapToDto(LocationNode locationNode);

    List<LocationDto> mapToDtos(List<LocationNode> locationNodes);

    ShortLocationDto mapToShortDto(LocationNode locationNode);
}
