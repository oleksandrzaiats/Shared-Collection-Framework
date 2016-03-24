package com.scf.server.application.security.token;

import com.scf.shared.dto.UserDTO;

public interface TokenHandler {

    UserDTO parseUserFromToken(String token);

    String createTokenForUser(UserDTO user);
}
