package ru.noleg.scootrent.entity.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import ru.noleg.scootrent.entity.scooter.Scooter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "t_location_node")
public class LocationNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private Long id;

    @Column(name = "c_title", nullable = false, length = 50)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "c_location_type", nullable = false)
    private LocationType locationType;

    @Column(name = "c_latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "c_longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "c_address", length = 100)
    private String address;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_parent_id")
    private LocationNode parent;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<LocationNode> children = new ArrayList<>();

    @OneToMany(mappedBy = "rentalPoint")
    private Set<Scooter> scooters = new HashSet<>();

    public LocationNode() {
    }

    public LocationNode(Long id,
                        String title,
                        LocationType locationType,
                        BigDecimal latitude,
                        BigDecimal longitude,
                        String address,
                        LocationNode parent,
                        List<LocationNode> children,
                        Set<Scooter> scooters) {
        this.id = id;
        this.title = title;
        this.locationType = locationType;
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

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocationNode getParent() {
        return parent;
    }

    public void setParent(LocationNode parent) {
        this.parent = parent;
    }

    public List<LocationNode> getChildren() {
        return children;
    }

    public void setChildren(List<LocationNode> children) {
        this.children = children;
    }

    public Set<Scooter> getScooters() {
        return scooters;
    }

    public void setScooters(Set<Scooter> scooters) {
        this.scooters = scooters;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }
}

