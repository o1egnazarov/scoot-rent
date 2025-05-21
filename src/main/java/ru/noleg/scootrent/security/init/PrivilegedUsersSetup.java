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

@Component
@Transactional
public class PrivilegedUsersSetup implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(PrivilegedUsersSetup.class);

    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final String ADMIN_PHONE = "+79999999999";
    public static final String MODERATOR_USERNAME = "moderator";
    public static final String MODERATOR_EMAIL = "moderator@gmail.com";
    public static final String MODERATOR_PHONE = "+78888888888";


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
        initUser(ADMIN_USERNAME, ADMIN_EMAIL, ADMIN_PHONE, adminPassword, Role.ROLE_ADMIN);
        initUser(MODERATOR_USERNAME, MODERATOR_EMAIL, MODERATOR_PHONE, moderatorPassword, Role.ROLE_MODERATOR);
    }

    private void initUser(String username, String email, String phone, String password, Role role) {

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
        user.setPhone(phone);
        user.setRole(role);

        this.userRepository.save(user);
        logger.info("Created default user: '{}'.", username);
    }
}