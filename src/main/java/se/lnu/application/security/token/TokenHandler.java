package se.lnu.application.security.token;

import se.lnu.application.model.dto.UserDTO;

public interface TokenHandler {

    UserDTO parseUserFromToken(String token);

    String createTokenForUser(UserDTO user);
}
