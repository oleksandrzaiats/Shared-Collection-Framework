package se.lnu.application.dao;

import org.springframework.stereotype.Repository;
import se.lnu.application.entity.CollectionEntity;

@Repository
public class CollectionDAO extends AbstractDAO<CollectionEntity> {
    public CollectionDAO() {
        super(CollectionEntity.class);
    }
}
