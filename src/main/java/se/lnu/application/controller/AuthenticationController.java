/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.lnu.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.lnu.application.dto.UserDTO;
import se.lnu.application.processor.UserProcessor;
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
    String createTokenForUser(@RequestParam("login") String login, @RequestParam("password") String password, HttpServletResponse response) {
        UserDTO authUser = userProcessor.findUserByLogin(login);

        if (authUser != null & authUser.getPassword().equals(password)) {
            return tokenAuthenticationService.addAuthentication(response, authUser);
        } else {
            return "User isn't found";
        }
    }
}

