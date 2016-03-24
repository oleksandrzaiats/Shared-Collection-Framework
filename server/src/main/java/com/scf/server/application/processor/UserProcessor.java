package com.scf.server.application.processor;

import com.scf.server.application.model.converter.UserConverter;
import com.scf.server.application.security.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.scf.server.application.model.dao.UserDAO;
import com.scf.shared.dto.UserDTO;
import com.scf.server.application.model.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all "business logic" for Users and managing database.
 */
@Component
public class UserProcessor implements Processor<UserDTO> {

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserConverter userConverter;

    @Override
    public List<UserDTO> getAll(AuthUser user) {
        return userConverter.convertToDTOList(userDAO.getList(new ArrayList<>()));
    }

    @Override
    public UserDTO get(Long id) {
        return userConverter.convertToDTO(userDAO.get(id));
    }

    @Override
    public UserDTO create(UserDTO dto) {
        UserEntity userEntity = userConverter.convertToEntity(dto);
        userEntity = userDAO.create(userEntity);
        return userConverter.convertToDTO(userEntity);
    }

    @Override
    public UserDTO update(UserDTO dto, AuthUser user) {
        UserEntity userEntity = userConverter.convertToEntity(dto);
        userEntity = userDAO.update(userEntity);
        return userConverter.convertToDTO(userEntity);
    }

    @Override
    public void delete(Long id, AuthUser user) {
        UserEntity userEntity = userDAO.get(id);
        userDAO.delete(userEntity);
    }


    public UserDTO findUserByLogin(String login) {
        UserEntity userEntity = userDAO.findUserByLogin(login);
        if (userEntity != null) {
            return userConverter.convertToDTO(userEntity);
        } else {
            return null;
        }
    }
}
