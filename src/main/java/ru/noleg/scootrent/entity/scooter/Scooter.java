package ru.noleg.scootrent.entity.scooter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import ru.noleg.scootrent.entity.location.LocationNode;

import java.time.Duration;

@Entity
@Table(name = "t_scooter")
public class Scooter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private Long id;

    @Column(name = "c_number_plate", nullable = false, unique = true, length = 10)
    private String numberPlate;

    @Column(name = "c_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ScooterStatus status = ScooterStatus.FREE;

    @Column(name = "c_duration_in_used")
    private Duration durationInUsed = Duration.ZERO;

    @ManyToOne(optional = false, fetch = FetchType.LAZY) // TODO или просто запрос который подгружает все
    @JoinColumn(name = "c_model_id", nullable = false)
    private ScooterModel model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_rental_point_id")
    private LocationNode rentalPoint;

    public Scooter() {
    }

    public Scooter(Long id,
                   String numberPlate,
                   ScooterStatus status,
                   Duration durationInUsed,
                   ScooterModel model,
                   LocationNode rentalPoint
    ) {
        this.id = id;
        this.numberPlate = numberPlate;
        this.status = status;
        this.durationInUsed = durationInUsed;
        this.model = model;
        this.rentalPoint = rentalPoint;
    }

    public void addDurationInUsed(Duration usedTime) {
        this.durationInUsed = this.durationInUsed.plus(usedTime);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public ScooterStatus getStatus() {
        return status;
    }

    public void setStatus(ScooterStatus status) {
        this.status = status;
    }

    public Duration getDurationInUsed() {
        return durationInUsed;
    }

    public void setDurationInUsed(Duration durationInUsed) {
        this.durationInUsed = durationInUsed;
    }

    public ScooterModel getModel() {
        return model;
    }

    public void setModel(ScooterModel model) {
        this.model = model;
    }

    public LocationNode getRentalPoint() {
        return rentalPoint;
    }

    public void setRentalPoint(LocationNode locationNode) {
        this.rentalPoint = locationNode;
    }
}
