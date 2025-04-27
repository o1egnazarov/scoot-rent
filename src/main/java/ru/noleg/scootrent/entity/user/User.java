package ru.noleg.scootrent.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import ru.noleg.scootrent.entity.UserSubscription;
import ru.noleg.scootrent.entity.UserTariff;
import ru.noleg.scootrent.entity.rental.Rental;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private Long id;

    @Column(name = "c_username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "c_email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "c_password")
    private String password;

    @Column(name = "c_phone", nullable = false, length = 16, unique = true)
    private String phone;

    @Column(name = "c_date_of_birthday", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "c_role")
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Rental> rentals = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserTariff> userTariffs = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserSubscription> userSubscriptions = new ArrayList<>();

    public User() {
    }

    public User(Long id,
                String username,
                String email,
                String password,
                String phone,
                LocalDate dateOfBirth,
                Role role,
                List<Rental> rentals,
                List<UserTariff> userTariffs,
                List<UserSubscription> userSubscriptions) {

        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.rentals = rentals;
        this.userTariffs = userTariffs;
        this.userSubscriptions = userSubscriptions;
    }

    public void addRental(Rental rental) {
        this.rentals.add(rental);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }

    public List<UserTariff> getUserTariffs() {
        return userTariffs;
    }

    public void setUserTariffs(List<UserTariff> tariffs) {
        this.userTariffs = tariffs;
    }

    public List<UserSubscription> getUserSubscriptions() {
        return userSubscriptions;
    }

    public void setUserSubscriptions(List<UserSubscription> userSubscriptions) {
        this.userSubscriptions = userSubscriptions;
    }
}
