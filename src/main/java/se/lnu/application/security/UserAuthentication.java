package se.lnu.application.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Represents info for an authentication request and for an authenticated principal
 */
public class UserAuthentication implements Authentication {

    private final AuthUser user;
    private boolean authenticated = true;

    public UserAuthentication(AuthUser user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return user.getLogin();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return user.getPassword();
    }

    @Override
    public AuthUser getDetails() {
        return user;
    }

    @Override
    public Object getPrincipal() {
        return user.getLogin();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
