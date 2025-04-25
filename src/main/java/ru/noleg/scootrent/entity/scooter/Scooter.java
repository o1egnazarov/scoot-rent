package ru.noleg.scootrent.entity.scooter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.rental.RentalPoint;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_scooter")
public class Scooter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "c_id")
    private Long id;

    @Column(name = "c_number_plate", nullable = false, length = 10)
    private String numberPlate;

    @Column(name = "c_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ScooterStatus status = ScooterStatus.FREE;

    @Column(name = "c_duration_in_used")
    private Duration durationInUsed = Duration.ZERO;

    @ManyToOne(optional = false)
    @JoinColumn(name = "c_model", nullable = false)
    private ScooterModel model;

    @ManyToOne
    @JoinColumn(name = "c_rental_point")
    private RentalPoint rentalPoint;

    @OneToMany(mappedBy = "scooter")
    private List<Rental> rentals = new ArrayList<>();

    public Scooter() {
    }

    public Scooter(Long id,
                   String numberPlate,
                   ScooterStatus status,
                   Duration durationInUsed,
                   ScooterModel model,
                   RentalPoint rentalPoint,
                   List<Rental> rentals) {
        this.id = id;
        this.numberPlate = numberPlate;
        this.status = status;
        this.durationInUsed = durationInUsed;
        this.model = model;
        this.rentals = rentals;
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


    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }

    public RentalPoint getRentalPoint() {
        return rentalPoint;
    }

    public void setRentalPoint(RentalPoint rentalPoint) {
        this.rentalPoint = rentalPoint;
    }
}
