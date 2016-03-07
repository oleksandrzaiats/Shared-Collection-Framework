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
        return null; // TODO
    }

    @Override
    public ArtifactDTO convertToDTO(ArtifactEntity entity) {
        return null; // TODO
    }
}
