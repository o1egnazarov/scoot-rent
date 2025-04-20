package ru.noleg.scootrent.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.UserNotFoundException;
import ru.noleg.scootrent.repository.UserRepository;
import ru.noleg.scootrent.service.UserService;

import java.util.List;

@Service
public class UserServiceDefaultImpl implements UserService {

    @PersistenceContext
    private EntityManager entityManager;
    private final UserRepository userRepository;

    public UserServiceDefaultImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User save(User user) {

        return this.userRepository.save(user);
    }

    @Override
    public User getUser(Long id) {
        try {

            return this.userRepository.findById(id).orElseThrow(
                    () -> new UserNotFoundException("User with id: " + id + " not found.")
            );
        } catch (Exception e) {

            throw new ServiceException("Error on get user", e);
        }
    }


    public List<User> getAllUsers() {
        try {

            return this.userRepository.findAll();
        } catch (Exception e) {

            throw new ServiceException("Error on get users", e);
        }
    }
}
