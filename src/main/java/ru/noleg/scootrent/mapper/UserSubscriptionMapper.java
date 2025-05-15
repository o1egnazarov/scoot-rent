package ru.noleg.scootrent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.noleg.scootrent.dto.tariff.UserSubscriptionDto;
import ru.noleg.scootrent.entity.UserSubscription;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserSubscriptionMapper extends BaseMapper<UserSubscription, UserSubscriptionDto> {
    @Override
    UserSubscription mapToEntity(UserSubscriptionDto userSubscriptionDto);

    @Override
    @Mapping(source = "tariff.id", target = "tariffId")
    UserSubscriptionDto mapToDto(UserSubscription subscription);

    @Override
    List<UserSubscription> mapToEntities(List<UserSubscriptionDto> dtos);

    @Override
    List<UserSubscriptionDto> mapToDtos(List<UserSubscription> entities);
}
