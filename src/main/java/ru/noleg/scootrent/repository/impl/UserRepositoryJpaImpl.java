package ru.noleg.scootrent.repository.impl;

import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.RepositoryException;
import ru.noleg.scootrent.repository.UserRepository;
import ru.noleg.scootrent.repository.util.BaseRepositoryImpl;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryJpaImpl extends BaseRepositoryImpl<User, Long> implements UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryJpaImpl.class);

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected Long getEntityId(User entity) {
        return entity.getId();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {

            final String ql = """
                    SELECT u FROM User u
                    WHERE u.username = :username
                    """;

            TypedQuery<User> query = entityManager.createQuery(ql, User.class);
            query.setParameter("username", username);

            List<User> resultList = query.getResultList();
            return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
        } catch (Exception e) {
            logger.error("Failed to find user by username: {}.", username, e);
            throw new RepositoryException("Repository error on fetch user by username", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {

            final String ql = """
                    SELECT u FROM User u
                    WHERE u.email = :email
                    """;

            TypedQuery<User> query = entityManager.createQuery(ql, User.class);
            query.setParameter("email", email);

            List<User> resultList = query.getResultList();
            return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
        } catch (Exception e) {
            logger.error("Failed to find user by email: {}.", email, e);
            throw new RepositoryException("Repository error on fetch user by email", e);
        }
    }
}
