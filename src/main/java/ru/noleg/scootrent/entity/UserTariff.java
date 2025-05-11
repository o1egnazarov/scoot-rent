package ru.noleg.scootrent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_user_tariffs")
public class UserTariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "c_user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "c_tariff_id")
    private Tariff tariff;

    @Column(name = "c_discount_pct")
    private Integer discountPct;

    @Transient
    private BigDecimal customPricePerMinute;

    @Column(name = "c_start_date")
    private LocalDateTime startDate;

    @Column(name = "c_end_date")
    private LocalDateTime endDate;

    @Transient
    public LocalDateTime getCalculatedEndDate() {
        if (tariff.getDurationUnit() == null || tariff.getDurationValue() == null) {
            return null;
        }

        return switch (tariff.getDurationUnit()) {
            case HOUR -> startDate.plusHours(tariff.getDurationValue());
            case DAY -> startDate.plusDays(tariff.getDurationValue());
            case WEEK -> startDate.plusWeeks(tariff.getDurationValue());
            case MONTH -> startDate.plusMonths(tariff.getDurationValue());
            case YEAR -> startDate.plusYears(tariff.getDurationValue());
        };
    }

    public UserTariff() {
    }

    public UserTariff(Long id,
                      User user,
                      Tariff tariff,
                      Integer discountPct,
                      BigDecimal customPricePerMinute,
                      LocalDateTime startDate) {
        this.id = id;
        this.user = user;
        this.tariff = tariff;
        this.discountPct = discountPct;
        this.customPricePerMinute = customPricePerMinute;
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public Integer getDiscountPct() {
        return discountPct;
    }

    public void setDiscountPct(Integer discountPct) {
        this.discountPct = discountPct;
    }

    public BigDecimal getCustomPricePerMinute() {
        return customPricePerMinute;
    }

    public void setCustomPricePerMinute(BigDecimal customPricePerMinute) {
        this.customPricePerMinute = customPricePerMinute;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime validFrom) {
        this.startDate = validFrom;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime validUntil) {
        this.endDate = validUntil;
    }
}