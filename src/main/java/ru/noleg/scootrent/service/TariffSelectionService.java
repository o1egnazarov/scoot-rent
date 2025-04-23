package ru.noleg.scootrent.service;

import ru.noleg.scootrent.service.impl.TariffSelectionServiceDefaultImpl;

public interface TariffSelectionService {
    TariffSelectionServiceDefaultImpl.SelectedTariff selectTariffForUser(Long userId);
}
