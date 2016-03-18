package se.lnu.application.security.token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import se.lnu.application.dto.UserDTO;
import se.lnu.application.security.AuthUser;
import se.lnu.application.security.UserAuthentication;

/**
 *
 * @author olefir
 */
@Service
public class TokenAuthenticationServiceImpl implements TokenAuthenticationService{

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
    private static final long TEN_DAYS = 1000 * 60 * 60 * 24 * 10;

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
                Authentication tempVar = new UserAuthentication(new AuthUser(user));
                return tempVar;
            }
        }
        return null;
    }
}
