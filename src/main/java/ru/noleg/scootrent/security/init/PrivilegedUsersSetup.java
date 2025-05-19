package ru.noleg.scootrent.security.init;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.user.Role;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.repository.UserRepository;

import java.time.LocalDate;

@Component
@Transactional
public class PrivilegedUsersSetup implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(PrivilegedUsersSetup.class);

    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final String MODERATOR_USERNAME = "moderator";
    public static final String MODERATOR_EMAIL = "moderator@gmail.com";


    @Value("${app.admin.password:admin123}")
    private String adminPassword;
    @Value("${app.moderator.password:moder123}")
    private String moderatorPassword;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PrivilegedUsersSetup(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        initUser(ADMIN_USERNAME, ADMIN_EMAIL,"+79999999999", adminPassword, Role.ROLE_ADMIN);
        initUser(MODERATOR_USERNAME, MODERATOR_EMAIL, "+78888888888" ,moderatorPassword, Role.ROLE_MODERATOR);
    }

    private void initUser(String username, String email, String phone ,String password, Role role) {

        if (this.userRepository.findByUsername(username).isPresent()) {
            logger.info("User '{}' already exists, skipping.", username);
            return;
        }

        if (this.userRepository.findByEmail(email).isPresent()) {
            logger.warn("Email '{}' already used, skipping user '{}'.", email, username);
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(this.passwordEncoder.encode(password));
        user.setRole(role);
        // TODO просто в миграциях not null уберу и это надо не забыть убрать
        user.setDateOfBirth(LocalDate.of(2004, 8, 8));
        user.setPhone(phone);

        this.userRepository.save(user);
        logger.info("Created default user: '{}'.", username);
    }
}