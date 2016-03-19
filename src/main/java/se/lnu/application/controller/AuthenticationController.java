package se.lnu.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lnu.application.dto.UserDTO;
import se.lnu.application.processor.UserProcessor;
import se.lnu.application.security.UserRole;
import se.lnu.application.security.token.TokenAuthenticationService;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthenticationController extends AbstractController {

    @Autowired
    UserProcessor userProcessor;

    @Autowired
    TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> login(@RequestParam("login") String login, @RequestParam("password") String password, HttpServletResponse response) {
        // TODO change @RequestParam to @RequestBody UserDTO
        // TODO validate DTO
        UserDTO authUser = userProcessor.findUserByLogin(login);
        if (authUser != null & authUser.getPassword().equals(password)) {
            return new ResponseEntity<>(tokenAuthenticationService.addAuthentication(response, authUser), HttpStatus.OK);
        } else {
            // TODO throw RecordNotFoundException exception
            return new ResponseEntity<>("User isn't found", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> create(@RequestParam("login") String login, @RequestParam("name") String name, @RequestParam("password") String password, HttpServletResponse response) {
        // TODO change @RequestParam to @RequestBody UserDTO
        // TODO validate DTO
        UserDTO authUser = userProcessor.findUserByLogin(login);

        if (authUser == null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setLogin(login);
            userDTO.setName(name);
            userDTO.setPassword(password);
            // TODO role must be set if there is no ROLE in UserDTO in request
            userDTO.setRole(UserRole.ROLE_USER.toString());

            userDTO = userProcessor.create(userDTO);

            if (userDTO != null) {
                // TODO POST method must return entity which was created (return UserDTO)
                return new ResponseEntity<>("User is created", HttpStatus.OK);
            } else {
                // TODO remove
                return new ResponseEntity<>("User isn't created", HttpStatus.BAD_REQUEST);
            }
        } else {
            // TODO throw UserAlreadyExist exception
            return new ResponseEntity<>("User exits", HttpStatus.BAD_REQUEST);
        }
    }
}

