package ru.noleg.scootrent.service;

import java.math.BigDecimal;

public interface TariffAssignmentService {
    void assignTariffToUser(Long userId, Long tariffId, BigDecimal customPrice, Integer discountPct);
}
