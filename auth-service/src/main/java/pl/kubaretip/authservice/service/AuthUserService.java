package pl.kubaretip.authservice.service;

import pl.kubaretip.authservice.domain.AuthUser;

public interface AuthUserService {
    AuthUser createAuthUser(String username, String password);
}
