package ru.noleg.scootrent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_user_tariffs")
public class UserTariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO подумать над двойной или одинарной связью
    @ManyToOne(optional = false)
    @JoinColumn(name = "c_user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "c_tariff_id")
    private Tariff tariff;

    @Column(name = "c_discount_pct")
    private Integer discountPct;

    // TODO или BigDecimal
    @Transient
    private Integer customPricePerUnit;

    @Column(name = "c_valid_from")
    private LocalDateTime validFrom;

    @Column(name = "c_valid_untill")
    private LocalDateTime validUntil;

    public UserTariff() {
    }

    public UserTariff(Long id,
                      User user,
                      Tariff tariff,
                      Integer discountPct,
                      Integer customPricePerUnit,
                      LocalDateTime validFrom,
                      LocalDateTime validUntil) {
        this.id = id;
        this.user = user;
        this.tariff = tariff;
        this.discountPct = discountPct;
        this.customPricePerUnit = customPricePerUnit;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
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

    public Integer getCustomPricePerUnit() {
        return customPricePerUnit;
    }

    public void setCustomPricePerUnit(Integer customPricePerUnit) {
        this.customPricePerUnit = customPricePerUnit;
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
}