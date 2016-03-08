package se.lnu.application.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.lnu.application.converter.ArtifactConverter;
import se.lnu.application.dao.ArtifactDAO;
import se.lnu.application.dto.ArtifactDTO;
import se.lnu.application.entity.ArtifactEntity;

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
    public List<ArtifactDTO> getAll() {
        List<ArtifactEntity> artifactEntityList = artifactDAO.getList();
        return artifactConverter.convertToDTOList(artifactEntityList);
    }

    @Override
    public ArtifactDTO get(Long id) {
        ArtifactEntity artifactEntity = artifactDAO.get(id);
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
        artifactDAO.delete(artifactEntity);
    }
}
