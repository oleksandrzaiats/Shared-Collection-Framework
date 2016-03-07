package se.lnu.application.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.lnu.application.dto.CollectionDTO;
import se.lnu.application.entity.CollectionEntity;

/**
 * Converter for @{@link CollectionEntity} and @{@link CollectionDTO}
 */
@Component
public class CollectionConverter implements Converter<CollectionEntity, CollectionDTO> {

    @Autowired
    ArtifactConverter artifactConverter;

    @Override
    public CollectionEntity convertToEntity(CollectionDTO dto) {
        CollectionEntity collectionEntity = new CollectionEntity();
        collectionEntity.setId(dto.getId());
        collectionEntity.setKey(dto.getKey());
        collectionEntity.setName(dto.getName());
        if (dto.getArtifactList() != null) {
            collectionEntity.setArtifactList(artifactConverter.convertToEntityList(dto.getArtifactList()));
        }
        if (dto.getCollectionList() != null) {
            collectionEntity.setCollectionList(convertToEntityList(dto.getCollectionList()));
        }
        return collectionEntity;
    }

    @Override
    public CollectionDTO convertToDTO(CollectionEntity entity) {
        CollectionDTO collectionDTO = new CollectionDTO();
        collectionDTO.setId(entity.getId());
        collectionDTO.setKey(entity.getKey());
        collectionDTO.setName(entity.getName());
        if (entity.getArtifactList() != null) {
            collectionDTO.setArtifactList(artifactConverter.convertToDTOList(entity.getArtifactList()));
        }
        if (entity.getCollectionList() != null) {
            collectionDTO.setCollectionList(convertToDTOList(entity.getCollectionList()));
        }
        return collectionDTO;
    }
}
