package se.lnu.application.security;

import se.lnu.application.dto.UserDTO;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an authenticated user
 */
public class AuthUser {

    private UserDTO user;

    public AuthUser(UserDTO user) {
        this.user = user;
    }

    private Set<UserAuthority> authorities;

    public void setRoles(Set<UserRole> roles) {
        for (UserRole role : roles) {
            grantRole(role);
        }
    }

    public void grantRole(UserRole role) {
        if (authorities == null) {
            authorities = new HashSet<>();
        }
        authorities.add(role.asAuthorityFor(this));
    }

    public Set<UserAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = new HashSet<>();
        }
        authorities.add(UserRole.ROLE_USER.asAuthorityFor(this));
        return authorities;
    }

    public boolean hasRole(UserRole role) {
        return authorities.contains(role.asAuthorityFor(this));
    }

    public Set<UserRole> getRoles() {
        Set<UserRole> roles = EnumSet.noneOf(UserRole.class);
        if (authorities != null) {
            for (UserAuthority authority : authorities) {
                roles.add(UserRole.valueOf(authority));
            }
        }
        return roles;
    }

    public String getPassword() {
        return user.getPassword();
    }

    public String getLogin() {
        return user.getLogin();
    }
}
