package se.lnu.application.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;
import se.lnu.application.converter.UserConverter;
import se.lnu.application.dao.UserDAO;
import se.lnu.application.dto.UserDTO;
import se.lnu.application.entity.UserEntity;

import java.util.List;

/**
 * Created by olefir on 2016-03-17.
 */
@Component
public class UserProcessor implements Processor<UserDTO> {

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserConverter userConverter;

    @Override
    public List<UserDTO> getAll() {
        return userConverter.convertToDTOList(userDAO.getList());
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
        // TODO add artifact removing logic
        UserEntity userEntity = userConverter.convertToEntity(dto);
        userEntity = userDAO.update(userEntity);
        return userConverter.convertToDTO(userEntity);
    }

    @Override
    public void delete(Long id) {
        // TODO add deleting logic
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
