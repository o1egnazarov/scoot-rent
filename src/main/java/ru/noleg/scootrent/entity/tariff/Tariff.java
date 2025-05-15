package ru.noleg.scootrent.entity.tariff;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_tariff")
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private Long id;

    @Column(name = "c_title", nullable = false, length = 50)
    private String title;

    @Column(name = "c_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TariffType type;

    @Column(name = "c_billing_mode", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private BillingMode billingMode;

    @Column(name = "c_price_per_unit")
    private BigDecimal pricePerUnit;

    @Column(name = "c_unlock_fee")
    private int unlockFee;

    @Column(name = "c_duration_unit")
    @Enumerated(value = EnumType.STRING)
    private DurationType durationUnit;

    @Column(name = "c_duration_value")
    private Integer durationValue;

    @Column(name = "c_sub_duration_days")
    private Integer subDurationDays;

    @Column(name = "c_is_active")
    private Boolean isActive = true;

    @Column(name = "c_valid_from")
    private LocalDateTime validFrom;

    @Column(name = "c_valid_until")
    private LocalDateTime validUntil;

    public Tariff() {
    }

    public Tariff(Long id,
                  String title,
                  TariffType type,
                  BigDecimal pricePerUnit,
                  int unlockFee,
                  DurationType durationUnit,
                  Integer durationValue,
                  Integer subDurationDays,
                  Boolean isActive,
                  LocalDateTime validFrom,
                  LocalDateTime validUntil) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.pricePerUnit = pricePerUnit;
        this.unlockFee = unlockFee;
        this.durationUnit = durationUnit;
        this.durationValue = durationValue;
        this.subDurationDays = subDurationDays;
        this.isActive = isActive;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    public void deactivateTariff() {
        this.isActive = false;
        this.validUntil = LocalDateTime.now();
    }

    public void activateTariff() {
        this.isActive = true;
        this.validFrom = LocalDateTime.now();
        this.validUntil = LocalDateTime.now().plusYears(1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TariffType getType() {
        return type;
    }

    public void setType(TariffType type) {
        this.type = type;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public int getUnlockFee() {
        return unlockFee;
    }

    public void setUnlockFee(int unlockFee) {
        this.unlockFee = unlockFee;
    }

    public DurationType getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(DurationType durationUnit) {
        this.durationUnit = durationUnit;
    }

    public Integer getDurationValue() {
        return durationValue;
    }

    public void setDurationValue(Integer durationValue) {
        this.durationValue = durationValue;
    }

    public Integer getSubDurationDays() {
        return subDurationDays;
    }

    public void setSubDurationDays(Integer subDurationDays) {
        this.subDurationDays = subDurationDays;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public BillingMode getBillingMode() {
        return billingMode;
    }

    public void setBillingMode(BillingMode billingMode) {
        this.billingMode = billingMode;
    }
}
