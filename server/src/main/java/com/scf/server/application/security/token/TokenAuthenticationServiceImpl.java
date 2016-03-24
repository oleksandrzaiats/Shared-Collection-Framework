package com.scf.server.application.security.token;

import com.scf.server.application.security.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.scf.shared.dto.UserDTO;
import com.scf.server.application.security.UserAuthentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Contains methods for adding/reading a token to/from a header
 */
@Service
public class TokenAuthenticationServiceImpl implements TokenAuthenticationService {

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    @Autowired
    private TokenHandler tokenHandler;

    public String addAuthentication(HttpServletResponse response, UserDTO user) {
        String token = tokenHandler.createTokenForUser(user);
        response.addHeader(AUTH_HEADER_NAME, token);
        return token;
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null) {
            UserDTO user = tokenHandler.parseUserFromToken(token);
            if (user != null) {
                return new UserAuthentication(new AuthUser(user));
            }
        }
        return null;
    }
}
