package se.lnu.application.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.lnu.application.model.converter.ArtifactConverter;
import se.lnu.application.model.dao.ArtifactDAO;
import se.lnu.application.model.dto.ArtifactDTO;
import se.lnu.application.model.entity.ArtifactEntity;
import se.lnu.application.model.exception.ErrorCode;
import se.lnu.application.model.exception.RecordNotFoundException;
import se.lnu.application.security.AuthUser;

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
        if(artifactEntity == null) {
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
    public ArtifactDTO update(ArtifactDTO dto) {
        ArtifactEntity artifactEntity = artifactDAO.update(artifactConverter.convertToEntity(dto));
        return artifactConverter.convertToDTO(artifactEntity);
    }

    @Override
    public void delete(Long id) {
        ArtifactEntity artifactEntity = artifactDAO.get(id);
        if(artifactEntity == null) {
            throw new RecordNotFoundException(ErrorCode.ARTIFACT_NOT_FOUND);
        }
        artifactDAO.delete(artifactEntity);
    }
}
