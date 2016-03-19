package se.lnu.application.security.token;

import se.lnu.application.dto.UserDTO;

public interface TokenHandler {

    UserDTO parseUserFromToken(String token);

    String createTokenForUser(UserDTO user);
}
