package ru.noleg.scootrent.service.auth;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.repository.UserRepository;
import ru.noleg.scootrent.security.jwt.JwtTokenProvider;

@Service
@Transactional
public class AuthenticationServiceJwtImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceJwtImpl.class);

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceJwtImpl(UserRepository userRepository,
                                        UserDetailsService userDetailsService,
                                        PasswordEncoder passwordEncoder,
                                        JwtTokenProvider jwtTokenProvider,
                                        AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Long signUp(User user) {
        logger.debug("Signing up user: {}.", user.getUsername());

        this.validateUserData(user);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        Long userId = this.userRepository.save(user).getId();

        logger.debug("User successfully signUp with id: {}.", userId);
        return userId;
    }

    private void validateUserData(User user) {
        if (this.userRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.error("User with username {} already exists.", user.getUsername());
            throw new BusinessLogicException("User with username: " + user.getUsername() + " already exists.");
        }

        if (this.userRepository.findByEmail(user.getEmail()).isPresent()) {
            logger.error("User with email {} already exists.", user.getEmail());
            throw new BusinessLogicException("User with email: " + user.getEmail() + " already exists.");
        }

        if (this.userRepository.findByPhone(user.getPhone()).isPresent()) {
            logger.error("User with phone {} already exists.", user.getPhone());
            throw new BusinessLogicException("User with phone: " + user.getPhone() + " already exists.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String signIn(String username, String password) {
        logger.debug("Signing up user: {}.", username);

        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username, password
        ));

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        String token = this.jwtTokenProvider.generateToken(userDetails);

        logger.debug("User: {}, successfully signIn.", username);
        return token;
    }
}