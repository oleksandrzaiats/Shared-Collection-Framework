package se.lnu.application.dao;

import org.springframework.stereotype.Repository;
import se.lnu.application.entity.ArtifactEntity;

@Repository
public class ArtifactDAO extends AbstractDAO<ArtifactEntity> {
    public ArtifactDAO() {
        super(ArtifactEntity.class);
    }
}
