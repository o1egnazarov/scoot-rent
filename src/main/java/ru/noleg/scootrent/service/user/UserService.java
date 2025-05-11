package ru.noleg.scootrent.service.user;

import ru.noleg.scootrent.entity.user.User;

import java.util.List;

public interface UserService {
    User save(User user);

    User getUser(Long id);

    List<User> getAllUsers();
}
