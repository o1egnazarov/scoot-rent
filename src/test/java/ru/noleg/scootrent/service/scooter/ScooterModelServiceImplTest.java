package ru.noleg.scootrent.service.scooter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.noleg.scootrent.entity.scooter.ScooterModel;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.repository.ScooterModelRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScooterModelServiceImplTest {

    @Mock
    private ScooterModelRepository scooterModelRepository;

    @InjectMocks
    private ScooterModelServiceImpl scooterModelService;


    @Test
    void add_shouldSaveScooterModelAndReturnId() {
        // Arrange
        ScooterModel model = mock(ScooterModel.class);
        ScooterModel savedModel = mock(ScooterModel.class);

        when(this.scooterModelRepository.save(model)).thenReturn(savedModel);
        when(savedModel.getId()).thenReturn(1L);

        // Act
        Long result = this.scooterModelService.add(model);

        // Assert
        assertEquals(1L, result);
        verify(this.scooterModelRepository, times(1)).save(model);
        verify(savedModel, times(1)).getId();
    }

    @Test
    void delete_shouldRemoveScooterModel_whenExists() {
        // Arrange
        Long id = 1L;
        when(this.scooterModelRepository.existsById(id)).thenReturn(true);
        doNothing().when(this.scooterModelRepository).delete(id);

        // Act
        this.scooterModelService.delete(id);

        // Assert
        verify(this.scooterModelRepository, times(1)).existsById(id);
        verify(this.scooterModelRepository, times(1)).delete(id);
    }

    @Test
    void delete_shouldThrownException_whenModelNotExists() {
        // Arrange
        Long id = 100L;
        when(this.scooterModelRepository.existsById(id)).thenReturn(false);

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> scooterModelService.delete(id));
        assertEquals("Scooter model with id 100 not found.", ex.getMessage());

        verify(this.scooterModelRepository, times(1)).existsById(id);
        verify(this.scooterModelRepository, never()).delete(any());
    }

    @Test
    void getScooterModel_shouldReturnModel_whenExists() {
        // Arrange
        Long id = 2L;
        ScooterModel model = mock(ScooterModel.class);
        when(this.scooterModelRepository.findById(id)).thenReturn(Optional.of(model));

        // Act
        ScooterModel result = this.scooterModelService.getScooterModel(id);

        // Assert
        assertEquals(model, result);
        verify(this.scooterModelRepository, times(1)).findById(id);
    }

    @Test
    void getScooterModel_shouldThrownException_whenNotFound() {
        // Arrange
        Long id = 100L;
        when(this.scooterModelRepository.findById(id)).thenReturn(Optional.empty());

        // Act | Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> this.scooterModelService.getScooterModel(id));
        assertEquals("Scooter model with id: 100 not found.", ex.getMessage());

        verify(this.scooterModelRepository, times(1)).findById(id);
    }

    @Test
    void getAllScooterModels_shouldReturnListOfModels() {
        // Arrange
        List<ScooterModel> models = List.of(mock(ScooterModel.class), mock(ScooterModel.class));
        when(this.scooterModelRepository.findAll()).thenReturn(models);

        // Act
        List<ScooterModel> result = this.scooterModelService.getAllScooterModels();

        // Assert
        assertEquals(2, result.size());
        verify(this.scooterModelRepository, times(1)).findAll();
    }
}
