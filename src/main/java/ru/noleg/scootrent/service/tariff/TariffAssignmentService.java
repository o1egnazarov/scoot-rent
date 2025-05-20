package ru.noleg.scootrent.service.tariff;

import ru.noleg.scootrent.entity.tariff.UserTariff;

import java.math.BigDecimal;

public interface TariffAssignmentService {
    void assignTariffToUser(Long userId, Long tariffId, BigDecimal customPrice, Integer discountPct);

    UserTariff getActiveUserTariff(Long userId);

    void canselTariffFromUser(Long userId);
}
