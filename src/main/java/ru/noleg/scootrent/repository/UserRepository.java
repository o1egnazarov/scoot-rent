package ru.noleg.scootrent.repository;

import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.repository.util.BaseRepository;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String email);
}
