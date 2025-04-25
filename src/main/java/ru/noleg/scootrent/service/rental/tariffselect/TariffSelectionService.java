package ru.noleg.scootrent.service.rental.tariffselect;

public interface TariffSelectionService {
    TariffSelectionServiceImpl.SelectedTariff selectTariffForUser(Long userId);
}
