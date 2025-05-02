package ru.noleg.scootrent.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.noleg.scootrent.dto.user.UpdateUserDto;
import ru.noleg.scootrent.dto.user.UserDto;
import ru.noleg.scootrent.entity.user.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper extends BaseMapper<User, UserDto> {
    @Override
    User mapToEntity(UserDto userDto);

    @Override
    List<User> mapToEntities(List<UserDto> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UpdateUserDto dto, @MappingTarget User entity);

    @Override
    UserDto mapToDto(User user);

    @Override
    List<UserDto> mapToDtos(List<User> entities);
}
