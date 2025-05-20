package ru.noleg.scootrent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.noleg.scootrent.dto.rental.ShortRentalDto;
import ru.noleg.scootrent.entity.rental.Rental;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ScooterMapper.class, TariffMapper.class, LocationMapper.class})
public interface RentalMapper extends BaseMapper<Rental, ShortRentalDto> {
    @Override
    ShortRentalDto mapToDto(Rental rental);

    @Override
    List<ShortRentalDto> mapToDtos(List<Rental> entities);
}
