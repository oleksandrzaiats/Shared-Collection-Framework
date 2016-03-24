package com.scf.server.application.controller;

import com.scf.server.application.processor.UserProcessor;
import com.scf.server.application.security.UserRole;
import com.scf.shared.dto.UserDTO;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractControllerTest extends Assert {

    @Autowired
    protected UserProcessor userProcessor;

    protected UserDTO getUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("test");
        userDTO.setRole(UserRole.ROLE_USER.toString());
        userDTO.setLogin("login");
        userDTO.setPassword("as123tesT*");

        return userProcessor.create(userDTO);
    }
}
