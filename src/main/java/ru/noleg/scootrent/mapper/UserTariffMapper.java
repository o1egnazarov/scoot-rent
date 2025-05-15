package ru.noleg.scootrent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.noleg.scootrent.dto.tariff.UserTariffDto;
import ru.noleg.scootrent.entity.UserTariff;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserTariffMapper extends BaseMapper<UserTariff, UserTariffDto> {
    @Override
    UserTariff mapToEntity(UserTariffDto userTariffDto);

    @Override
    @Mapping(source = "tariff.id", target = "tariffId")
    UserTariffDto mapToDto(UserTariff userTariff);

    @Override
    List<UserTariff> mapToEntities(List<UserTariffDto> dtos);

    @Override
    List<UserTariffDto> mapToDtos(List<UserTariff> entities);
}
