package ru.noleg.scootrent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.noleg.scootrent.dto.RentalDto;
import ru.noleg.scootrent.entity.rental.Rental;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ScooterMapper.class, TariffMapper.class, RentalPointMapper.class})
public interface RentalMapper extends BaseMapper<Rental, RentalDto> {
    @Override
    Rental mapToEntity(RentalDto rentalDto);

    @Override
    RentalDto mapToDto(Rental rental);

    @Override
    List<Rental> mapToEntities(List<RentalDto> dtos);

    @Override
    List<RentalDto> mapToDtos(List<Rental> entities);
}
