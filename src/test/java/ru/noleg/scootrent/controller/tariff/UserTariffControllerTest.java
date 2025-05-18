package ru.noleg.scootrent.controller.tariff;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.noleg.scootrent.dto.tariff.AssignTariffDto;
import ru.noleg.scootrent.dto.tariff.UserTariffDto;
import ru.noleg.scootrent.entity.UserTariff;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.mapper.UserTariffMapper;
import ru.noleg.scootrent.service.user.UserDetailsImpl;
import ru.noleg.scootrent.service.tariff.TariffAssignmentService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTariffControllerTest {

    @Mock
    private TariffAssignmentService tariffAssignmentService;

    @Mock
    private UserTariffMapper userTariffMapper;

    @InjectMocks
    private UserTariffController userTariffController;


    @Test
    void assignTariff_shouldAssignSuccessfully() {
        // Arrange
        Long tariffId = 1L;
        AssignTariffDto dto = new AssignTariffDto(1L, BigDecimal.valueOf(10), 15);

        // Act
        ResponseEntity<Void> response = this.userTariffController.assignTariff(tariffId, dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(this.tariffAssignmentService, times(1))
                .assignTariffToUser(dto.userId(), tariffId, dto.customPricePerUnit(), dto.discountPct());
    }

    @Test
    void assignUserToTariff_shouldThrownException_whenUserOrTariffNotFound() {
        // Arrange
        Long tariffId = 1L;
        AssignTariffDto dto = new AssignTariffDto(100L, BigDecimal.valueOf(10), 15);

        doThrow(new NotFoundException("User or tariff not found"))
                .when(this.tariffAssignmentService).assignTariffToUser(dto.userId(), tariffId, dto.customPricePerUnit(), dto.discountPct());

        // Act | Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.userTariffController.assignTariff(tariffId, dto)
        );

        assertEquals("User or tariff not found", exception.getMessage());

        verify(this.tariffAssignmentService, times(1))
                .assignTariffToUser(dto.userId(), tariffId, dto.customPricePerUnit(), dto.discountPct());
        verifyNoMoreInteractions(this.tariffAssignmentService);
    }

    @Test
    void canselSpecialTariffAsModerator_shouldCancelSuccessfully() {
        // Arrange
        Long userId = 1L;

        // Act
        ResponseEntity<Void> response = this.userTariffController.canselTariffAsModerator(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(this.tariffAssignmentService, times(1)).canselTariffFromUser(userId);
    }

    @Test
    void canselSpecialTariffAsModerator_shouldThrownException_whenUserNotFound() {
        // Act
        Long userId = 100L;

        doThrow(new NotFoundException("User not found"))
                .when(this.tariffAssignmentService).canselTariffFromUser(userId);

        // Act | Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.userTariffController.canselTariffAsModerator(userId)
        );

        assertEquals("User not found", exception.getMessage());

        verify(this.tariffAssignmentService, times(1)).canselTariffFromUser(userId);
        verifyNoMoreInteractions(this.tariffAssignmentService);
    }

    @Test
    void canselSpecialTariffAsUser_shouldCancelSuccessfully() {
        // Arrange
        Long userId = 1L;
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);

        // Act
        ResponseEntity<Void> response = this.userTariffController.canselTariffAsUser(userDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(this.tariffAssignmentService, times(1)).canselTariffFromUser(userId);
    }

    @Test
    void canselSpecialTariffAsUser_shouldThrownException_whenSpecialTariffNotFound() {
        // Arrange
        Long userId = 42L;
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);

        doThrow(new NotFoundException("No special tariff found"))
                .when(this.tariffAssignmentService).canselTariffFromUser(userId);

        // Act | Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.userTariffController.canselTariffAsUser(userDetails)
        );

        assertEquals("No special tariff found", exception.getMessage());

        verify(this.tariffAssignmentService, times(1)).canselTariffFromUser(userId);
        verifyNoMoreInteractions(this.tariffAssignmentService);
    }

    @Test
    void getActiveSpecialTariffAsModerator_shouldReturnSpecialTariff() {
        // Arrange
        Long userId = 1L;
        UserTariff specialTariff = mock(UserTariff.class);
        UserTariffDto specialTariffDto = mock(UserTariffDto.class);

        when(this.tariffAssignmentService.getActiveUserTariff(userId)).thenReturn(specialTariff);
        when(this.userTariffMapper.mapToDto(specialTariff)).thenReturn(specialTariffDto);

        // Act
        ResponseEntity<UserTariffDto> response = this.userTariffController.getActiveSpecialTariffAsModerator(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(specialTariffDto, response.getBody());

        verify(this.tariffAssignmentService, times(1)).getActiveUserTariff(userId);
        verify(this.userTariffMapper, times(1)).mapToDto(specialTariff);
    }

    @Test
    void getActiveSpecialTariffAsModerator_shouldThrownException_whenUserHasNoSpecialTariff() {
        // Arrange
        Long userId = 100L;

        when(this.tariffAssignmentService.getActiveUserTariff(userId))
                .thenThrow(new NotFoundException("No active special tariff"));

        // Act | Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.userTariffController.getActiveSpecialTariffAsModerator(userId)
        );

        assertEquals("No active special tariff", exception.getMessage());

        verify(this.tariffAssignmentService, times(1)).getActiveUserTariff(userId);
        verifyNoMoreInteractions(this.tariffAssignmentService);
    }

    @Test
    void getActiveSpecialTariffAsUser_shouldReturnSpecialTariff() {
        // Arrange
        Long userId = 42L;

        UserTariff specialTariff = mock(UserTariff.class);
        UserTariffDto specialTariffDto = mock(UserTariffDto.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);

        when(this.tariffAssignmentService.getActiveUserTariff(userId)).thenReturn(specialTariff);
        when(this.userTariffMapper.mapToDto(specialTariff)).thenReturn(specialTariffDto);

        // Act
        ResponseEntity<UserTariffDto> response = this.userTariffController.getActiveSpecialTariffAsUser(userDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(specialTariffDto, response.getBody());

        verify(this.tariffAssignmentService, times(1)).getActiveUserTariff(userId);
        verify(this.userTariffMapper, times(1)).mapToDto(specialTariff);
    }

    @Test
    void getActiveSpecialTariffAsUser_shouldThrownException_whenUserHasNoSpecialTariff() {
        // Arrange
        Long userId = 42L;
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);

        // Act | Assert
        when(tariffAssignmentService.getActiveUserTariff(userId))
                .thenThrow(new NotFoundException("No active special tariff"));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.userTariffController.getActiveSpecialTariffAsUser(userDetails)
        );

        assertEquals("No active special tariff", exception.getMessage());

        verify(tariffAssignmentService, times(1)).getActiveUserTariff(userId);
        verifyNoMoreInteractions(this.tariffAssignmentService);
    }
}