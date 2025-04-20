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
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.entity.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_rental")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "c_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "c_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "c_scooter_id")
    private Scooter scooter;

    @Column(name = "c_start_time")
    private LocalDateTime startTime;

    @Column(name = "c_end_time")
    private LocalDateTime endTime;

    @Transient
    private Integer cost;
}

// TODO додумать Tariff, UserTariff, UserSub