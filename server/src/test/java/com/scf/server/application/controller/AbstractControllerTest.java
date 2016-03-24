package com.scf.server.application.controller;

import com.scf.server.application.InitialValue;
import com.scf.server.application.model.converter.UserConverter;
import com.scf.server.application.processor.UserProcessor;
import com.scf.shared.dto.UserDTO;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractControllerTest extends Assert {

    @Autowired
    protected UserProcessor userProcessor;
    @Autowired
    protected UserConverter userConverter;

    protected UserDTO getUser() {
        UserDTO userDTO = userConverter.convertToDTO(InitialValue.getUserEntity());
        return userProcessor.create(userDTO);
    }
}
