package se.lnu.application.security.token;

import se.lnu.application.dto.UserDTO;

/**
 * Created by olefir on 2016-03-18.
 */
public interface TokenHandler {

    public UserDTO parseUserFromToken(String token);

    public String createTokenForUser(UserDTO user);
}
