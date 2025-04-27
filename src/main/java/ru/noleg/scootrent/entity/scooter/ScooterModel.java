package ru.noleg.scootrent.entity.scooter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_scooter_model")
public class ScooterModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private Long id;

    @Column(name = "c_title", nullable = false, length = 50)
    private String title;

    @Column(name = "c_max_speed", nullable = false)
    private Integer maxSpeed;

    @Column(name = "c_range_km", nullable = false)
    private Integer rangeKm;

    public ScooterModel() {
    }

    public ScooterModel(Long id,
                        String title,
                        Integer maxSpeed,
                        Integer rangeKm) {
        this.id = id;
        this.title = title;
        this.maxSpeed = maxSpeed;
        this.rangeKm = rangeKm;
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

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Integer getRangeKm() {
        return rangeKm;
    }

    public void setRangeKm(Integer rangeKm) {
        this.rangeKm = rangeKm;
    }
}
