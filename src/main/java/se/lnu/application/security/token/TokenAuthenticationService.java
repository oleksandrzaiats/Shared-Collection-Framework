package se.lnu.application.security.token;

import org.springframework.security.core.Authentication;
import se.lnu.application.dto.UserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TokenAuthenticationService {
    String addAuthentication(HttpServletResponse response, UserDTO user);

    Authentication getAuthentication(HttpServletRequest request);
}
