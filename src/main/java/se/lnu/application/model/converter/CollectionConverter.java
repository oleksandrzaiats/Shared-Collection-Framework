package se.lnu.application.model.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.lnu.application.model.dto.CollectionDTO;
import se.lnu.application.model.entity.CollectionEntity;

/**
 * Converter for @{@link CollectionEntity} and @{@link CollectionDTO}
 */
@Component
public class CollectionConverter implements Converter<CollectionEntity, CollectionDTO> {

    @Autowired
    ArtifactConverter artifactConverter;
    @Autowired
    UserConverter userConverter;

    @Override
    public CollectionEntity convertToEntity(CollectionDTO dto) {
        CollectionEntity collectionEntity = new CollectionEntity();
        collectionEntity.setId(dto.getId());
        collectionEntity.setKey(dto.getKey());
        collectionEntity.setName(dto.getName());
        collectionEntity.setUser(userConverter.convertToEntity(dto.getUser()));
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
        collectionDTO.setUser(userConverter.convertToDTO(entity.getUser()));
        if (entity.getArtifactList() != null) {
            collectionDTO.setArtifactList(artifactConverter.convertToDTOList(entity.getArtifactList()));
        }
        if (entity.getCollectionList() != null) {
            collectionDTO.setCollectionList(convertToDTOList(entity.getCollectionList()));
        }
        return collectionDTO;
    }
}
