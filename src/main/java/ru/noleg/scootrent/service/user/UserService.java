package ru.noleg.scootrent.service.user;

import ru.noleg.scootrent.entity.user.Role;
import ru.noleg.scootrent.entity.user.User;

import java.util.List;

public interface UserService {
    User save(User user);

    void delete(Long id);

    User getUser(Long id);

    List<User> getAllUsers();

    void updateUserRole(Long userId, Role role);
}
