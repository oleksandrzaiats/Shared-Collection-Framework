package se.lnu.application.dao;

import org.springframework.stereotype.Repository;
import se.lnu.application.entity.ArtifactEntity;
import se.lnu.application.entity.CollectionEntity;
import se.lnu.application.entity.UserEntity;

import java.util.List;

/**
 * Created by olefir on 2016-03-17.
 */

@Repository
public class UserDAO extends AbstractDAO<UserEntity> {
    public UserDAO() {
        super(UserEntity.class);
    }

    public UserEntity findUserByLogin(String login) {
        List<?> list = getHibernateTemplate().find("from " + "user" + " where login=?", login);
        if (list.size() > 0) {
            return (UserEntity) list.get(0);
        }
        return null;
    }
}