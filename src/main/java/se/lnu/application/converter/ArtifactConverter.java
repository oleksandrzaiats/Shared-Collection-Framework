package se.lnu.application.converter;

import org.springframework.stereotype.Component;
import se.lnu.application.dto.ArtifactDTO;
import se.lnu.application.entity.ArtifactEntity;

/**
 * Converter for @{@link ArtifactEntity} and @{@link ArtifactDTO}
 */
@Component
public class ArtifactConverter implements Converter<ArtifactEntity, ArtifactDTO> {
    @Override
    public ArtifactEntity convertToEntity(ArtifactDTO dto) {
        ArtifactEntity artifactEntity = new ArtifactEntity();
        artifactEntity.setId(dto.getId());
        artifactEntity.setName(dto.getName());
        artifactEntity.setFileName(dto.getFileName());
        artifactEntity.setContentType(dto.getContentType());
        artifactEntity.setFile(dto.getFileBytes());
        return artifactEntity;
    }

    @Override
    public ArtifactDTO convertToDTO(ArtifactEntity entity) {
        ArtifactDTO artifactDTO = new ArtifactDTO();
        artifactDTO.setId(entity.getId());
        artifactDTO.setName(entity.getName());
        artifactDTO.setFileName(entity.getFileName());
        artifactDTO.setContentType(entity.getContentType());
        artifactDTO.setFileBytes(entity.getFile());
        return artifactDTO;
    }
}
