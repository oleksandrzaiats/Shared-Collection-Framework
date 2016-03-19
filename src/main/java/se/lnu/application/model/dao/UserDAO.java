package se.lnu.application.model.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se.lnu.application.model.entity.UserEntity;

import java.util.List;

@Repository
public class UserDAO extends AbstractDAO<UserEntity> {
    public UserDAO() {
        super(UserEntity.class);
    }

    @Transactional
    public UserEntity findUserByLogin(String login) {
        List<?> list = getHibernateTemplate().find("from " + getTableName() + " where login=?", login);
        if (list.size() > 0) {
            return (UserEntity) list.get(0);
        }
        return null;
    }
}