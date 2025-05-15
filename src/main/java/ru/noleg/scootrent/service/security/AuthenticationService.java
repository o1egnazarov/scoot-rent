package ru.noleg.scootrent.service.security;

import ru.noleg.scootrent.entity.user.User;

public interface AuthenticationService {
    Long signUp(User user);

    String signIn(String username, String password);
}
