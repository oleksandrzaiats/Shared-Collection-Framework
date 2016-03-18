/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author olefir
 */
@RestController
public class AuthenticationController {

    @Autowired
    UserProcessor userProcessor;

    @Autowired
    TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?>  createTokenForUser(@RequestParam("login") String login, @RequestParam("password") String password, HttpServletResponse response) {
        UserDTO authUser = userProcessor.findUserByLogin(login);

        if (authUser != null & authUser.getPassword().equals(password)) {
            return new ResponseEntity<>(tokenAuthenticationService.addAuthentication(response, authUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User isn't found", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> createTokenForUser(@RequestParam("login") String login, @RequestParam("name") String name, @RequestParam("password") String password, HttpServletResponse response) {
        UserDTO authUser = userProcessor.findUserByLogin(login);

        if (authUser == null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setLogin(login);
            userDTO.setName(name);
            userDTO.setPassword(password);
            userDTO.setRole(UserRole.USER.toString());

            userDTO = userProcessor.create(userDTO);

            if (userDTO != null) {
                return new ResponseEntity<>("User is created", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User isn't created", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("User exits", HttpStatus.BAD_REQUEST);
        }
    }
}

