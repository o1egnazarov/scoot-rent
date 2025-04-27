package ru.noleg.scootrent.service.tariff;

import java.math.BigDecimal;

public interface TariffAssignmentService {
    void assignTariffToUser(Long userId, Long tariffId, BigDecimal customPrice, Integer discountPct);
}
