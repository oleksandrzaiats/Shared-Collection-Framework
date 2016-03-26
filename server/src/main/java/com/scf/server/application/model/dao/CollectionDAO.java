package com.scf.server.application.model.dao;

import com.scf.server.application.model.entity.ArtifactEntity;
import com.scf.server.application.model.entity.CollectionEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CollectionDAO extends AbstractDAO<CollectionEntity> {
    public CollectionDAO() {
        super(CollectionEntity.class);
    }

    @Transactional
    public CollectionEntity getBySharedKey(String sharedKey) {
        List<?> list = getHibernateTemplate().find("from " + getTableName() + " where shared_key=?", sharedKey);
        if (list.size() > 0) {
            return (CollectionEntity) list.get(0);
        }
        return null;
    }
}
