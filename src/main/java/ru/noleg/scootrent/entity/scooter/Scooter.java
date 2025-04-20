package ru.noleg.scootrent.entity;

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

import java.time.Duration;

@Entity
@Table(name = "t_scooter")
public class Scooter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "c_id")
    private Long id;

    @Column(name = "c_number_plate", nullable = false)
    private String numberPlate;

    @Column(name = "c_status")
    @Enumerated(value = EnumType.STRING)
    private ScooterStatus status;

    @Column(name = "c_duration_in_used")
    private Duration durationInUsed;

    @ManyToOne
    @JoinColumn(name = "c_model")
    private ScooterModel model;

    public Scooter() {
    }

    public Scooter(Long id,
                   String numberPlate,
                   ScooterStatus status,
                   Duration durationInUsed,
                   ScooterModel model) {
        this.id = id;
        this.numberPlate = numberPlate;
        this.status = status;
        this.durationInUsed = durationInUsed;
        this.model = model;
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
}
