package com.scf.server.application.model.dao;

import com.scf.server.application.model.entity.ArtifactEntity;
import org.springframework.stereotype.Repository;

@Repository
public class ArtifactDAO extends AbstractDAO<ArtifactEntity> {
    public ArtifactDAO() {
        super(ArtifactEntity.class);
    }
}
