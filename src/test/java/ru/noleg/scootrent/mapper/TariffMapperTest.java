package ru.noleg.scootrent.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.noleg.scootrent.dto.tariff.ShortTariffDto;
import ru.noleg.scootrent.dto.tariff.TariffDto;
import ru.noleg.scootrent.dto.tariff.UpdateTariffDto;
import ru.noleg.scootrent.entity.tariff.BillingMode;
import ru.noleg.scootrent.entity.tariff.DurationType;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.tariff.TariffType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TariffMapperTest {

    private final TariffMapper tariffMapper = Mappers.getMapper(TariffMapper.class);

    @Test
    void mapToEntity_shouldMapCorrectly() {
        TariffDto dto = new TariffDto(
                1L,
                "Weekend Free",
                TariffType.SPECIAL_TARIFF,
                BillingMode.PER_MINUTE,
                new BigDecimal("5.25"),
                5,
                DurationType.WEEK,
                2,
                30,
                true,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(6)
        );

        Tariff entity = this.tariffMapper.mapToEntity(dto);

        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.title(), entity.getTitle());
        assertEquals(dto.type(), entity.getType());
        assertEquals(dto.billingMode(), entity.getBillingMode());
        assertEquals(dto.pricePerUnit(), entity.getPricePerUnit());
        assertEquals(dto.unlockFee(), entity.getUnlockFee());
        assertEquals(dto.durationUnit(), entity.getDurationUnit());
        assertEquals(dto.durationValue(), entity.getDurationValue());
        assertEquals(dto.subDurationDays(), entity.getSubDurationDays());
        assertEquals(dto.isActive(), entity.getIsActive());
        assertEquals(dto.validFrom(), entity.getValidFrom());
        assertEquals(dto.validUntil(), entity.getValidUntil());
    }

    @Test
    void mapToDto_shouldMapCorrectly() {
        Tariff entity = new Tariff();
        entity.setId(1L);
        entity.setTitle("Weekend Free");
        entity.setType(TariffType.SPECIAL_TARIFF);
        entity.setBillingMode(BillingMode.PER_MINUTE);
        entity.setPricePerUnit(new BigDecimal("5.25"));
        entity.setUnlockFee(5);
        entity.setDurationUnit(DurationType.WEEK);
        entity.setDurationValue(2);
        entity.setSubDurationDays(30);
        entity.setIsActive(true);
        entity.setValidFrom(LocalDateTime.now());
        entity.setValidUntil(LocalDateTime.now().plusMonths(6));

        TariffDto dto = this.tariffMapper.mapToDto(entity);

        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getTitle(), dto.title());
        assertEquals(entity.getType(), dto.type());
        assertEquals(entity.getBillingMode(), dto.billingMode());
        assertEquals(entity.getPricePerUnit(), dto.pricePerUnit());
        assertEquals(entity.getUnlockFee(), dto.unlockFee());
        assertEquals(entity.getDurationUnit(), dto.durationUnit());
        assertEquals(entity.getDurationValue(), dto.durationValue());
        assertEquals(entity.getSubDurationDays(), dto.subDurationDays());
        assertEquals(entity.getIsActive(), dto.isActive());
        assertEquals(entity.getValidFrom(), dto.validFrom());
        assertEquals(entity.getValidUntil(), dto.validUntil());
    }

    @Test
    void updateTariffFromDto_shouldUpdateNonNullFieldsOnly() {
        Tariff entity = new Tariff();
        entity.setTitle("Old Title");
        entity.setPricePerUnit(new BigDecimal("3.33"));
        entity.setUnlockFee(10);
        entity.setDurationUnit(DurationType.DAY);
        entity.setDurationValue(5);
        entity.setSubDurationDays(15);
        entity.setValidFrom(LocalDateTime.now().minusDays(1));
        entity.setValidUntil(LocalDateTime.now().plusDays(1));

        UpdateTariffDto updateDto = new UpdateTariffDto(
                "New Title",
                TariffType.SPECIAL_TARIFF,
                null,
                20,
                DurationType.WEEK,
                7,
                30,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(2)
        );

        this.tariffMapper.updateTariffFromDto(updateDto, entity);

        assertEquals(entity.getTitle(), updateDto.title());
        assertEquals(entity.getType(), updateDto.type());
        assertEquals(entity.getPricePerUnit(), new BigDecimal("3.33"));
        assertEquals(entity.getUnlockFee(), updateDto.unlockFee());
        assertEquals(entity.getDurationUnit(), updateDto.durationUnit());
        assertEquals(entity.getDurationValue(), updateDto.durationValue());
        assertEquals(entity.getSubDurationDays(), updateDto.subDurationDays());
        assertEquals(entity.getValidFrom(), updateDto.validFrom());
        assertEquals(entity.getValidUntil(), updateDto.validUntil());
    }

    @Test
    void mapToShortDto_shouldMapCorrectly() {
        Tariff entity = new Tariff();
        entity.setId(10L);
        entity.setTitle("Short Tariff");
        entity.setPricePerUnit(new BigDecimal("2.5"));
        entity.setUnlockFee(3);
        entity.setType(TariffType.SPECIAL_TARIFF);
        entity.setBillingMode(BillingMode.PER_HOUR);
        entity.setSubDurationDays(7);
        entity.setValidFrom(LocalDateTime.now());
        entity.setValidUntil(LocalDateTime.now().plusDays(30));

        ShortTariffDto dto = this.tariffMapper.mapToShortDto(entity);

        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getTitle(), dto.title());
        assertEquals(entity.getPricePerUnit(), dto.pricePerUnit());
        assertEquals(entity.getUnlockFee(), dto.unlockFee());
        assertEquals(entity.getType(), dto.type());
        assertEquals(entity.getBillingMode(), dto.billingMode());
        assertEquals(entity.getSubDurationDays(), dto.subDurationDays());
        assertEquals(entity.getValidFrom(), dto.validFrom());
        assertEquals(entity.getValidUntil(), dto.validUntil());
    }

}