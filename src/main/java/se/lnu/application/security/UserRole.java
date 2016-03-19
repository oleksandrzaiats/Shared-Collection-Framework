package se.lnu.application.security;

public enum UserRole {
    ROLE_USER, ROLE_ADMIN;

    public UserAuthority asAuthorityFor(final AuthUser user) {
        final UserAuthority authority = new UserAuthority();
        authority.setAuthority(toString());
        authority.setUser(user);
        return authority;
    }

    public static UserRole valueOf(final UserAuthority authority) {
        try {
            return UserRole.valueOf(authority.getAuthority());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No role defined for authority: " + authority.getAuthority());
        }
    }
}
