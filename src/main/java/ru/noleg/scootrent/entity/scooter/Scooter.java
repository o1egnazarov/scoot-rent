package ru.noleg.scootrent.entity.scooter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.rental.RentalPoint;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Duration durationInUsed;

    @ManyToOne(optional = false)
    @JoinColumn(name = "c_model", nullable = false)
    private ScooterModel model;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "t_scooter_rental_point",
            joinColumns = @JoinColumn(name = "c_scooter_id", referencedColumnName = "c_id"),
            inverseJoinColumns = @JoinColumn(name = "c_rental_point_id", referencedColumnName = "c_id"))
    @JsonIgnore
    private Set<RentalPoint> rentalPoints = new HashSet<>();

    @OneToMany(mappedBy = "scooter")
    private List<Rental> rentals = new ArrayList<>();

    public Scooter() {
    }

    public Scooter(Long id,
                   String numberPlate,
                   ScooterStatus status,
                   Duration durationInUsed,
                   ScooterModel model,
                   Set<RentalPoint> rentalPoints,
                   List<Rental> rentals) {
        this.id = id;
        this.numberPlate = numberPlate;
        this.status = status;
        this.durationInUsed = durationInUsed;
        this.model = model;
        this.rentalPoints = rentalPoints;
        this.rentals = rentals;
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

    public Set<RentalPoint> getRentalPoints() {
        return rentalPoints;
    }

    public void setRentalPoints(Set<RentalPoint> rentalPoints) {
        this.rentalPoints = rentalPoints;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }
}
