package se.lnu.application.security.token;

import org.springframework.security.core.Authentication;
import se.lnu.application.dto.UserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by olefir on 2016-03-18.
 */
public interface TokenAuthenticationService {
    public String addAuthentication(HttpServletResponse response, UserDTO user);

    public Authentication getAuthentication(HttpServletRequest request);
}
