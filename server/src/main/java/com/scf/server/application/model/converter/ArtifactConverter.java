package com.scf.server.application.model.converter;

import com.scf.shared.dto.ArtifactDTO;
import com.scf.server.application.model.entity.ArtifactEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Converter for @{@link ArtifactEntity} and @{@link ArtifactDTO}
 */
@Component
public class ArtifactConverter implements Converter<ArtifactEntity, ArtifactDTO> {

    @Autowired
    UserConverter userConverter;

    @Override
    public ArtifactEntity convertToEntity(ArtifactDTO dto) {
        ArtifactEntity artifactEntity = new ArtifactEntity();
        artifactEntity.setId(dto.getId());
        artifactEntity.setName(dto.getName());
        artifactEntity.setFileName(dto.getFileName());
        artifactEntity.setContentType(dto.getContentType());
        artifactEntity.setFile(dto.getFileBytes());
        artifactEntity.setUser(userConverter.convertToEntity(dto.getUser()));
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
        artifactDTO.setUser(userConverter.convertToDTO(entity.getUser()));
        return artifactDTO;
    }
}
