
package ru.noleg.scootrent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_user_subscriptions")
public class UserSubscription {

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

    @Column(name = "c_minute_usage_limit")
    private int minuteUsageLimit;

    @Column(name = "c_minutes_used")
    private int minutesUsed;

    @Column(name = "c_start_date")
    private LocalDateTime startDate;

    @Column(name = "c_end_date")
    private LocalDateTime endDate;

    public UserSubscription() {
    }

    public UserSubscription(Long id,
                            User user,
                            Tariff tariff,
                            Integer minuteUsageLimit,
                            int minutesUsed,
                            LocalDateTime startDate,
                            LocalDateTime endDate) {
        this.id = id;
        this.user = user;
        this.tariff = tariff;
        this.minuteUsageLimit = minuteUsageLimit;
        this.minutesUsed = minutesUsed;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addMinutes(long minutes) {
        this.minutesUsed += (int) minutes;
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

    public Integer getMinuteUsageLimit() {
        return minuteUsageLimit;
    }

    public void setMinuteUsageLimit(Integer minuteUsageLimit) {
        this.minuteUsageLimit = minuteUsageLimit;
    }

    public int getMinutesUsed() {
        return minutesUsed;
    }

    public void setMinutesUsed(int minutesUsed) {
        this.minutesUsed = minutesUsed;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}