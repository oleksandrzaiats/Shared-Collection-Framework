package se.lnu.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lnu.application.dto.UserDTO;
import se.lnu.application.exception.ErrorCode;
import se.lnu.application.exception.RecordNotFoundException;
import se.lnu.application.exception.UserExistsException;
import se.lnu.application.processor.UserProcessor;
import se.lnu.application.security.UserRole;
import se.lnu.application.security.token.TokenAuthenticationService;

import javax.servlet.http.HttpServletResponse;

/**
 * Controller with operations for registration and authentication users.
 * Controller contains only request/response logic of API.
 * All "business logic" is in {@link UserProcessor}
 */
@RestController
public class AuthenticationController extends AbstractController {

    @Autowired
    UserProcessor userProcessor;

    @Autowired
    TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> login(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        validateBean(userDTO);
        UserDTO authUser = userProcessor.findUserByLogin(userDTO.getLogin());
        if (authUser != null && authUser.getPassword().equals(userDTO.getPassword())) {
            return new ResponseEntity<>(tokenAuthenticationService.addAuthentication(response, authUser), HttpStatus.OK);
        } else {
            throw new RecordNotFoundException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> create(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        validateBean(userDTO);
        UserDTO authUser = userProcessor.findUserByLogin(userDTO.getLogin());

        if (authUser == null) {
            if (userDTO.getRole() == null) {
                userDTO.setRole(UserRole.ROLE_USER.toString());
            }
            userDTO = userProcessor.create(userDTO);

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else {
            throw new UserExistsException(ErrorCode.USER_EXISTS);
        }
    }
}

