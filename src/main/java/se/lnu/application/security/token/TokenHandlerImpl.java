package se.lnu.application.security.token;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.lnu.application.dto.UserDTO;
import se.lnu.application.processor.UserProcessor;

/**
 *
 * @author olefir
 */
@Component
public class TokenHandlerImpl implements TokenHandler {

    @Autowired
    private UserProcessor userProcessor;

    private static final String SEPARATOR = "\\.";
    private static final String SECRET_KEY = "somekey";

    public UserDTO parseUserFromToken(String token) {
        try {
            String values = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            String email = values.split(SEPARATOR)[0];

            return userProcessor.findUserByLogin(email);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException x) {
            return null;
        }
    }

    public String createTokenForUser(UserDTO user) {
        return Jwts.builder()
                .setSubject(user.getLogin())
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}
