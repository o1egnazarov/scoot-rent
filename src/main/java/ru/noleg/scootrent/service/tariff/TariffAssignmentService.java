package ru.noleg.scootrent.service.tariff;

import ru.noleg.scootrent.entity.UserSubscription;
import ru.noleg.scootrent.entity.UserTariff;

import java.math.BigDecimal;

public interface TariffAssignmentService {
    void assignTariffToUser(Long userId, Long tariffId, BigDecimal customPrice, Integer discountPct);

    UserTariff getActiveUserTariff(Long userId);

    void canselTariffFromUser(Long userId);
}
