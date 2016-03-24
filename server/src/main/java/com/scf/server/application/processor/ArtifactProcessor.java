package com.scf.server.application.processor;

import com.scf.shared.dto.ArtifactDTO;
import com.scf.server.application.model.entity.ArtifactEntity;
import com.scf.server.application.security.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.scf.server.application.model.converter.ArtifactConverter;
import com.scf.server.application.model.dao.ArtifactDAO;
import com.scf.server.application.model.exception.ErrorCode;
import com.scf.server.application.model.exception.RecordNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all "business logic" for Artifacts and managing database.
 */
@Component
public class ArtifactProcessor implements Processor<ArtifactDTO> {

    @Autowired
    ArtifactDAO artifactDAO;

    @Autowired
    ArtifactConverter artifactConverter;

    @Override
    public List<ArtifactDTO> getAll(AuthUser user) {
        List<ArtifactEntity> artifactEntityList = artifactDAO.getList(new ArrayList<>());
        return artifactConverter.convertToDTOList(artifactEntityList);
    }

    @Override
    public ArtifactDTO get(Long id) {
        ArtifactEntity artifactEntity = artifactDAO.get(id);
        if (artifactEntity == null) {
            throw new RecordNotFoundException(ErrorCode.ARTIFACT_NOT_FOUND);
        }
        return artifactConverter.convertToDTO(artifactEntity);
    }

    @Override
    public ArtifactDTO create(ArtifactDTO dto) {
        ArtifactEntity artifactEntity = artifactDAO.create(artifactConverter.convertToEntity(dto));
        return artifactConverter.convertToDTO(artifactEntity);
    }

    @Override
    public ArtifactDTO update(ArtifactDTO dto, AuthUser user) {
        checkPermission(dto.getUser().getId(), user.getId());
        ArtifactEntity artifactEntity = artifactDAO.update(artifactConverter.convertToEntity(dto));
        return artifactConverter.convertToDTO(artifactEntity);
    }

    @Override
    public void delete(Long id, AuthUser user) {
        ArtifactEntity artifactEntity = artifactDAO.get(id);
        if (artifactEntity == null) {
            throw new RecordNotFoundException(ErrorCode.ARTIFACT_NOT_FOUND);
        }
        checkPermission(artifactEntity.getUser().getId(), user.getId());
        artifactDAO.delete(artifactEntity);
    }
}
