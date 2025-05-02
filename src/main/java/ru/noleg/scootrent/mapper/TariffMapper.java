package ru.noleg.scootrent.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.noleg.scootrent.dto.tariff.ShortTariffDto;
import ru.noleg.scootrent.dto.tariff.TariffDto;
import ru.noleg.scootrent.dto.tariff.UpdateTariffDto;
import ru.noleg.scootrent.entity.tariff.Tariff;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TariffMapper extends BaseMapper<Tariff, TariffDto> {
    @Override
    Tariff mapToEntity(TariffDto tariffDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTariffFromDto(UpdateTariffDto dto, @MappingTarget Tariff entity);

    @Override
    TariffDto mapToDto(Tariff tariff);

    ShortTariffDto mapToShortDto(Tariff tariff);

    @Override
    List<Tariff> mapToEntities(List<TariffDto> dtos);

    @Override
    List<TariffDto> mapToDtos(List<Tariff> entities);
}
