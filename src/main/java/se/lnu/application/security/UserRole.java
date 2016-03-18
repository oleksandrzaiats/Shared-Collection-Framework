package se.lnu.application.security;

public enum UserRole {
    USER, ADMIN;

    public UserAuthority asAuthorityFor(final AuthUser user) {
        final UserAuthority authority = new UserAuthority();
        authority.setAuthority("ROLE_" + toString());
        authority.setUser(user);
        return authority;
    }

    public static UserRole valueOf(final UserAuthority authority) {
        switch (authority.getAuthority()) {
            case "USER":
                return USER;
            case "ADMIN":
                return ADMIN;
        }
        throw new IllegalArgumentException("No role defined for authority: " + authority.getAuthority());
    }
}
