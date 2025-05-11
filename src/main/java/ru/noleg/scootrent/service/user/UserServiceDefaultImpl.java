package ru.noleg.scootrent.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.UserNotFoundException;
import ru.noleg.scootrent.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceDefaultImpl implements UserService {

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
        return this.userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with id: " + id + " not found.")
        );
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }
}
