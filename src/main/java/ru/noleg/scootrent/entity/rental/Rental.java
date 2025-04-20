package ru.noleg.scootrent.entity.rental;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import ru.noleg.scootrent.entity.UserSubscription;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_rental")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "c_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "c_user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "c_scooter_id")
    private Scooter scooter;

    @ManyToOne(optional = false)
    @JoinColumn(name = "c_tariff_id")
    private Tariff tariff;

    @ManyToOne
    @JoinColumn(name = "c_subscription_id")
    private UserSubscription subscription;

    @Column(name = "c_rental_status")
    @Enumerated(value = EnumType.STRING)
    private RentalStatus rentalStatus;

    @Column(name = "c_cost")
    private Integer cost;

    @Column(name = "c_start_time")
    private LocalDateTime startTime;

    @Column(name = "c_end_time")
    private LocalDateTime endTime;

    public Rental() {
    }

    public Rental(Long id,
                  User user,
                  Scooter scooter,
                  Tariff tariff,
                  UserSubscription subscription,
                  Integer cost,
                  LocalDateTime startTime,
                  LocalDateTime endTime) {
        this.id = id;
        this.user = user;
        this.scooter = scooter;
        this.tariff = tariff;
        this.subscription = subscription;
        this.cost = cost;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void stopRental(RentalPoint endPoint, Integer pricePerUnit) {
        if (this.rentalStatus == RentalStatus.COMPLETED) {
            throw new BusinessLogicException("Rental already completed.");
        }

        this.rentalStatus = RentalStatus.COMPLETED;
        this.endTime = LocalDateTime.now();
        this.cost = this.calculateCost(pricePerUnit);
    }

    // TODO допилить
    private Integer calculateCost(Integer pricePerUnit) {
        long duration = Duration.between(startTime, endTime).toMinutes();
        return pricePerUnit * (int) duration;
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

    public Scooter getScooter() {
        return scooter;
    }

    public void setScooter(Scooter scooter) {
        this.scooter = scooter;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public UserSubscription getSubscription() {
        return subscription;
    }

    public void setSubscription(UserSubscription subscription) {
        this.subscription = subscription;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
