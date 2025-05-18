package ru.noleg.scootrent.service.tariff;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.TariffRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TariffServiceImplTest {

    @Mock
    private TariffRepository tariffRepository;

    @InjectMocks
    private TariffServiceImpl tariffService;

    @Test
    void createTariff_shouldSaveAndReturnId() {
        // Arrange
        Tariff tariff = mock(Tariff.class);
        Tariff savedTariff = mock(Tariff.class);
        when(savedTariff.getId()).thenReturn(1L);

        when(this.tariffRepository.save(tariff)).thenReturn(savedTariff);

        // Act
        Long result = this.tariffService.createTariff(tariff);

        // Assert
        assertEquals(1L, result);
        verify(this.tariffRepository, times(1)).save(tariff);
    }

    @Test
    void getTariff_shouldReturnTariff_whenExists() {
        // Arrange
        Tariff tariff = mock(Tariff.class);
        when(tariff.getId()).thenReturn(1L);

        when(this.tariffRepository.findById(1L)).thenReturn(Optional.of(tariff));

        // Act
        Tariff result = this.tariffService.getTariff(1L);

        // Assert
        assertEquals(1L, result.getId());
        verify(this.tariffRepository, times(1)).findById(1L);
    }

    @Test
    void getTariff_shouldThrowException_whenNotFound() {
        // Arrange
        when(this.tariffRepository.findById(100L)).thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> tariffService.getTariff(100L));
        assertEquals("Tariff with id 100 not found", ex.getMessage());
    }

    @Test
    void getAllTariffs_shouldReturnList() {
        // Arrange
        List<Tariff> tariffs = List.of(new Tariff(), new Tariff());
        when(this.tariffRepository.findAll()).thenReturn(tariffs);

        // Act
        List<Tariff> result = this.tariffService.getAllTariffs();

        // Assert
        assertEquals(2, result.size());
        verify(this.tariffRepository, times(1)).findAll();
    }

    @Test
    void getActiveTariffs_shouldReturnList() {
        // Arrange
        List<Tariff> activeTariffs = List.of(new Tariff(), new Tariff());
        when(this.tariffRepository.findByActiveTrue()).thenReturn(activeTariffs);

        // Act
        List<Tariff> result = this.tariffService.getActiveTariffs();

        // Assert
        assertEquals(2, result.size());
        verify(this.tariffRepository, times(1)).findByActiveTrue();
    }

    @Test
    void deactivateTariff_shouldDeactivate_whenActive() {
        // Arrange
        Tariff tariff = mock(Tariff.class);
        when(tariff.getIsActive()).thenReturn(true);
        when(this.tariffRepository.findById(1L)).thenReturn(Optional.of(tariff));

        // Act
        this.tariffService.deactivateTariff(1L);

        // Assert
        verify(tariff, times(1)).deactivateTariff();
        verify(this.tariffRepository, times(1)).save(tariff);
    }

    @Test
    void deactivateTariff_shouldThrow_whenAlreadyInactive() {
        // Arrange
        Tariff tariff = mock(Tariff.class);
        when(tariff.getIsActive()).thenReturn(false);

        when(this.tariffRepository.findById(1L)).thenReturn(Optional.of(tariff));

        // Act | Assert
        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.tariffService.deactivateTariff(1L)
        );
        assertEquals("Tariff with id: 1 already deactivated.", ex.getMessage());

        verify(this.tariffRepository, times(1)).findById(1L);
        verify(tariff, never()).deactivateTariff();
    }

    @Test
    void activateTariff_shouldActivate_whenInactive() {
        // Arrange
        Tariff tariff = mock(Tariff.class);
        when(tariff.getIsActive()).thenReturn(false);

        when(this.tariffRepository.findById(1L)).thenReturn(Optional.of(tariff));

        // Act
        this.tariffService.activateTariff(1L);

        // Assert
        verify(tariff, times(1)).activateTariff();
        verify(this.tariffRepository, times(1)).save(tariff);
    }

    @Test
    void activateTariff_shouldThrow_whenAlreadyActive() {
        // Arrange
        Tariff tariff = mock(Tariff.class);
        when(tariff.getIsActive()).thenReturn(true);

        when(this.tariffRepository.findById(1L)).thenReturn(Optional.of(tariff));

        BusinessLogicException ex = assertThrows(BusinessLogicException.class,
                () -> this.tariffService.activateTariff(1L)
        );
        assertEquals("Tariff with id: 1 already activated.", ex.getMessage());

        verify(this.tariffRepository, times(1)).findById(1L);
        verify(tariff, never()).activateTariff();
    }
}