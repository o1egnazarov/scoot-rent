package ru.noleg.scootrent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.noleg.scootrent.dto.tariff.ShortTariffDto;
import ru.noleg.scootrent.dto.tariff.TariffDto;
import ru.noleg.scootrent.entity.tariff.Tariff;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TariffMapper extends BaseMapper<Tariff, TariffDto> {
    @Override
    Tariff mapToEntity(TariffDto tariffDto);

    @Override
    TariffDto mapToDetailDto(Tariff tariff);

    ShortTariffDto mapToShortDto(Tariff tariff);

    @Override
    List<Tariff> mapToEntities(List<TariffDto> dtos);

    @Override
    List<TariffDto> mapToDetailDtos(List<Tariff> entities);
}
