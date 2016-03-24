package se.lnu.application.model.dao;

import org.springframework.stereotype.Repository;
import se.lnu.application.model.entity.ArtifactEntity;
import se.lnu.application.model.entity.CollectionEntity;

import java.util.List;

@Repository
public class CollectionDAO extends AbstractDAO<CollectionEntity> {
    public CollectionDAO() {
        super(CollectionEntity.class);
    }

    public CollectionEntity getBySharedKey(String sharedKey) {
        List<?> list = getHibernateTemplate().find("from " + getTableName() + " where shared_key=?", sharedKey);
        if (list.size() > 0) {
            return (CollectionEntity) list.get(0);
        }
        return null;
    }
}
