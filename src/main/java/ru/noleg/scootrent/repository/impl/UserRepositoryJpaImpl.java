package ru.noleg.scootrent.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.RepositoryException;
import ru.noleg.scootrent.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryJpaImpl implements UserRepository {

    private final static Logger logger = LoggerFactory.getLogger(UserRepositoryJpaImpl.class);

    @PersistenceContext(unitName = "scoot-rent-unit")
    private EntityManager entityManager;

    @Override
    public User save(User user) {
        logger.info("Saving user with name: {}.", user.getUsername());

        try {

            if (user.getId() == null) {

                logger.debug("Persisting new user.");
                this.entityManager.persist(user);
            } else {

                logger.debug("Updating existing user with id={}.", user.getId());
                this.entityManager.merge(user);
            }

            this.entityManager.flush();

            logger.info("User saved successfully with id={}.", user.getId());
            return user;
        } catch (Exception e) {

            logger.error("Failed to save user. Error: {}.", e.getMessage());
            throw new RepositoryException("Repository error on save user.", e);
        }
    }

    @Override
    public void removeById(Long id) {
        logger.info("Removing user with id={}.", id);

        try {

            User user = this.entityManager.find(User.class, id);
            if (user == null) {

                logger.warn("No user found to delete with id={}.", id);
                throw new UserNotFoundException("not found");
            }

            this.entityManager.remove(user);
            logger.info("User remove successfully with id={}.", id);
        } catch (Exception e) {

            logger.error("Failed to remove user by id: {}. Error: {}.", id,
                    e.getMessage());
            throw new RepositoryException("Repository error on remove user by id.", e);
        }
    }

    @Override
    public List<User> getAll() {
        logger.info("Fetching all users.");
        final String qlString = "SELECT u FROM User u";

        try {

            List<User> users = this.entityManager.createQuery(qlString, User.class).getResultList();
            logger.info("Fetched {} users.", users.size());

            return users;
        } catch (Exception e) {

            logger.error("Failed to fetch users. Error: {}.", e.getMessage(), e);
            throw new RepositoryException("Repository error on get all users.", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        logger.info("Fetching user by id: {}.", id);

        try {

            User user = entityManager.find(User.class, id);
            return Optional.ofNullable(user);
        } catch (Exception e) {

            logger.error("Failed to fetch user by id. Error: {}", e.getMessage(), e);
            throw new RepositoryException("Repository error on get user by id.", e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        logger.info("Verify the user`s existence by id: {}.", id);

        try {

            User user = entityManager.find(User.class, id);
            return user != null;
        } catch (Exception e) {

            logger.error("Failed to verify the user`s existence by id. Error: {}", e.getMessage());
            throw new RepositoryException("Repository error on get user by id.", e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        logger.info("Fetching user by name: {}.", username);

        try {

            String jpql = "SELECT u FROM User u WHERE u.username = :username";
            TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
            query.setParameter("username", username);

            List<User> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {

            logger.error("Failed to fetch user by username. Error: {}", e.getMessage(), e);
            throw new RepositoryException("Repository error on get user by username.", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        logger.info("Fetching user by email: {}.", email);

        try {

            String jpql = "SELECT u FROM User u WHERE u.email = :email";
            TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
            query.setParameter("email", email);

            List<User> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {

            logger.error("Failed to fetch user by email. Error: {}", e.getMessage(), e);
            throw new RepositoryException("Repository error on get user by email.", e);
        }
    }
}
