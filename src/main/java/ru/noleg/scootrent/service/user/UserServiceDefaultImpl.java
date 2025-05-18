package ru.noleg.scootrent.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.user.Role;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.UserNotFoundException;
import ru.noleg.scootrent.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserServiceDefaultImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceDefaultImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public void delete(Long id) {

        User user = this.userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User with ID: " + id + " not found.")
        );

        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new BusinessLogicException("You can't delete an administrator.");
        }

        this.userRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return this.userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with id: " + id + " not found.")
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    // TODO изменил - проверить
    @Override
    public void updateUserRole(Long userId, Role role) {
        User user = this.getUser(userId);
        user.setRole(role);
        this.userRepository.save(user);
    }
}
