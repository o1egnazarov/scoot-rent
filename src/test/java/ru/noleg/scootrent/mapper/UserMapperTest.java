package ru.noleg.scootrent.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.noleg.scootrent.dto.security.SignUp;
import ru.noleg.scootrent.dto.user.UpdateUserDto;
import ru.noleg.scootrent.dto.user.UserDto;
import ru.noleg.scootrent.entity.user.Role;
import ru.noleg.scootrent.entity.user.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void mapToDto_shouldMapCorrectly() {
        // Arrange
        User user = new User(
                1L,
                "username",
                "email",
                "password",
                "phoneNumber",
                LocalDate.of(2025, 1, 1),
                Role.ROLE_USER
        );

        // Act
        UserDto userDto = this.userMapper.mapToDto(user);

        // Assert
        assertEquals(user.getId(), userDto.id());
        assertEquals(user.getUsername(), userDto.username());
        assertEquals(user.getEmail(), userDto.email());
        assertEquals(user.getPhone(), userDto.phone());
        assertEquals(user.getDateOfBirth(), userDto.dateOfBirth());
        assertEquals(user.getRole(), userDto.role());
    }

    @Test
    void mapToRegisterEntityFromSignUp_shouldMapCorrectly() {
        // Arrange
        SignUp signUp = new SignUp(
                "username",
                "email",
                "phoneNumber",
                "password",
                LocalDate.of(2025, 1, 1)
        );

        // Act
        User entity = this.userMapper.mapToRegisterEntityFromSignUp(signUp);

        // Assert
        assertNull(entity.getId());
        assertEquals(signUp.username(), entity.getUsername());
        assertEquals(signUp.email(), entity.getEmail());
        assertEquals(signUp.phone(), entity.getPhone());
        assertEquals(signUp.password(), entity.getPassword());
        assertEquals(signUp.dateOfBirth(), entity.getDateOfBirth());
        assertEquals(Role.ROLE_USER, entity.getRole());
    }

    @Test
    void mapToEntity_shouldMapCorrectly() {
        // Arrange
        UserDto dto = new UserDto(
                1L,
                "username",
                "email",
                "phoneNumber",
                LocalDate.of(2025, 1, 1),
                Role.ROLE_ADMIN
        );

        // Act
        User entity = this.userMapper.mapToEntity(dto);

        // Assert
        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.username(), entity.getUsername());
        assertEquals(dto.email(), entity.getEmail());
        assertEquals(dto.phone(), entity.getPhone());
        assertEquals(dto.dateOfBirth(), entity.getDateOfBirth());
        assertEquals(dto.role(), entity.getRole());
    }

    @Test
    void updateUserFromDto_shouldUpdateFields() {
        // Arrange
        User entity = new User(
                1L,
                "oldName",
                "old@mail.com",
                "oldPass",
                "oldPhone",
                LocalDate.of(2025, 1, 1),
                Role.ROLE_USER
        );

        UpdateUserDto dto = new UpdateUserDto(
                "newName", "new@mail.com", null, null
        );

        // Act
        this.userMapper.updateUserFromDto(dto, entity);

        // Assert
        assertEquals("newName", entity.getUsername());
        assertEquals("new@mail.com", entity.getEmail());
        assertEquals("oldPhone", entity.getPhone());
        assertEquals(LocalDate.of(2025, 1, 1), entity.getDateOfBirth());
    }

    @Test
    void updateUserFromDto_shouldIgnoreNulls() {
        // Arrange
        User entity = new User(
                1L,
                "oldName",
                "old@mail.com",
                "oldPass",
                "oldPhone",
                LocalDate.of(2025, 1, 1),
                Role.ROLE_USER
        );
        UpdateUserDto dto = new UpdateUserDto(
                null, null, null, null
        );

        // Act
        this.userMapper.updateUserFromDto(dto, entity);

        // Assert
        assertEquals("oldName", entity.getUsername());
        assertEquals("old@mail.com", entity.getEmail());
        assertEquals("oldPass", entity.getPassword());
        assertEquals("oldPhone", entity.getPhone());
        assertEquals(LocalDate.of(2025, 1, 1), entity.getDateOfBirth());
    }
}