package se.lnu.application.model.dao;

import org.springframework.stereotype.Repository;
import se.lnu.application.model.entity.ArtifactEntity;

@Repository
public class ArtifactDAO extends AbstractDAO<ArtifactEntity> {
    public ArtifactDAO() {
        super(ArtifactEntity.class);
    }
}
