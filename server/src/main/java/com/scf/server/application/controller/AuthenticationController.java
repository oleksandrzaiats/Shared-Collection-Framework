package com.scf.server.application.controller;

import com.scf.shared.dto.TokenDTO;
import com.scf.shared.dto.UserDTO;
import com.scf.server.application.model.exception.ErrorCode;
import com.scf.server.application.model.exception.RecordNotFoundException;
import com.scf.server.application.model.exception.UserExistsException;
import com.scf.server.application.processor.UserProcessor;
import com.scf.server.application.security.UserRole;
import com.scf.server.application.security.token.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            TokenDTO tokenDTO = new TokenDTO();
            tokenDTO.setToken(tokenAuthenticationService.addAuthentication(response, authUser));
            return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
        } else {
            throw new RecordNotFoundException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
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

