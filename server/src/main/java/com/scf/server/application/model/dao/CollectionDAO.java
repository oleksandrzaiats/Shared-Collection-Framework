package com.scf.server.application.model.dao;

import com.scf.server.application.model.entity.ArtifactEntity;
import com.scf.server.application.model.entity.CollectionEntity;
import org.springframework.stereotype.Repository;

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

    public void delete(CollectionEntity entity) {
        for (ArtifactEntity artifact : entity.getArtifactList()) {
            getHibernateTemplate().delete("from " + getTableName(ArtifactEntity.class) + " where id =" +
                    "(if ((select count(*) from " + getCATableName() + " where artifactList_id = ?) = 1, ?, -1))", artifact.getId());
        }
        super.delete(entity);
    }

    private String getCATableName() {
        return getTableName(CollectionEntity.class) + "_" + getTableName(ArtifactEntity.class);
    }
}
