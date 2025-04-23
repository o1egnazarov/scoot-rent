package ru.noleg.scootrent.entity.rental;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import ru.noleg.scootrent.entity.scooter.Scooter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// todo доработать с широтой и долготой
@Entity
@Table(name = "t_rental_point")
public class RentalPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "c_id")
    private Long id;

    @Column(name = "c_title", nullable = false, length = 50)
    private String title;

    @Column(name = "c_latitude")
    private Double latitude;

    @Column(name = "c_longitude")
    private Double longitude;

    @Column(name = "c_address", length = 100)
    private String address;

    @ManyToOne
    // TODO добавить адекватный parent_id
    @JoinColumn(name = "c_parent")
    @JsonIgnore
    private RentalPoint parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<RentalPoint> children = new ArrayList<>();

    @OneToMany(mappedBy = "rentalPoint")
    private Set<Scooter> scooters = new HashSet<>();

    public RentalPoint() {
    }

    public RentalPoint(Long id,
                       String title,
                       Double latitude,
                       Double longitude,
                       String address,
                       RentalPoint parent,
                       List<RentalPoint> children,
                       Set<Scooter> scooters) {
        this.id = id;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.parent = parent;
        this.children = children;
        this.scooters = scooters;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public RentalPoint getParent() {
        return parent;
    }

    public void setParent(RentalPoint parent) {
        this.parent = parent;
    }

    public List<RentalPoint> getChildren() {
        return children;
    }

    public void setChildren(List<RentalPoint> children) {
        this.children = children;
    }

    public Set<Scooter> getScooters() {
        return scooters;
    }

    public void setScooters(Set<Scooter> scooters) {
        this.scooters = scooters;
    }
}

