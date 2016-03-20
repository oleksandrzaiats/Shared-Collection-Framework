package se.lnu.application.security.token;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.lnu.application.model.dto.UserDTO;
import se.lnu.application.processor.UserProcessor;

/**
 * Contains methods for parsing and creating tokens
 */
@Component
public class TokenHandlerImpl implements TokenHandler {

    @Autowired
    private UserProcessor userProcessor;

    private static final String SECRET_KEY = "somekey";

    public UserDTO parseUserFromToken(String token) {
        try {
            String login = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return userProcessor.findUserByLogin(login);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public String createTokenForUser(UserDTO user) {
        return Jwts.builder()
                .setSubject(user.getLogin())
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}
