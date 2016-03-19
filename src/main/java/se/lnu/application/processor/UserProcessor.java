package se.lnu.application.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.lnu.application.model.converter.UserConverter;
import se.lnu.application.model.dao.UserDAO;
import se.lnu.application.model.dto.UserDTO;
import se.lnu.application.model.entity.UserEntity;
import se.lnu.application.security.AuthUser;

import java.util.ArrayList;
import java.util.List;

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
    public UserDTO update(UserDTO dto) {
        UserEntity userEntity = userConverter.convertToEntity(dto);
        userEntity = userDAO.update(userEntity);
        return userConverter.convertToDTO(userEntity);
    }

    @Override
    public void delete(Long id) {
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
