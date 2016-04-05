package com.scf.server.application.processor;

import com.scf.server.application.model.converter.CollectionConverter;
import com.scf.server.application.model.dao.ArtifactDAO;
import com.scf.server.application.model.dao.CollectionDAO;
import com.scf.server.application.model.entity.ArtifactEntity;
import com.scf.shared.dto.CollectionDTO;
import com.scf.server.application.model.entity.CollectionEntity;
import com.scf.server.application.model.exception.ErrorCode;
import com.scf.server.application.model.exception.RecordNotFoundException;
import com.scf.server.application.security.AuthUser;
import com.scf.server.application.security.UserRole;
import com.scf.server.application.utils.Filtering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all "business logic" for Collections and managing database.
 */
@Component
public class CollectionProcessor implements Processor<CollectionDTO> {

    @Autowired
    CollectionDAO collectionDAO;
    @Autowired
    ArtifactDAO artifactDAO;
    @Autowired
    CollectionConverter collectionConverter;

    @Override
    public List<CollectionDTO> getAll(AuthUser user) {
        List<Filtering> filteringList = new ArrayList<>();
        if (!user.getRoles().contains(UserRole.ROLE_ADMIN)) {
            Filtering userFilter = new Filtering("user_id", "=", user.getId().toString());
            filteringList.add(userFilter);
        }
        return collectionConverter.convertToDTOList(collectionDAO.getList(filteringList));
    }

    @Override
    public CollectionDTO get(Long id) {
        CollectionEntity collectionEntity = collectionDAO.get(id);
        if (collectionEntity == null) {
            throw new RecordNotFoundException(ErrorCode.COLLECTION_NOT_FOUND);
        }
        return collectionConverter.convertToDTO(collectionEntity);
    }

    @Override
    public CollectionDTO create(CollectionDTO dto) {
        CollectionEntity collectionEntity = collectionConverter.convertToEntity(dto);
        collectionEntity = collectionDAO.create(collectionEntity);
        return collectionConverter.convertToDTO(collectionEntity);
    }

    @Override
    public CollectionDTO update(CollectionDTO dto, AuthUser user) {
        CollectionEntity collectionEntity = collectionConverter.convertToEntity(dto);
        checkPermission(collectionEntity.getUser().getId(), user.getId());
        CollectionEntity oldCollectionEntity = collectionDAO.get(collectionEntity.getId());
        List<ArtifactEntity> removedArtifactList = getRemovedArtifact(collectionEntity, oldCollectionEntity);
        collectionEntity = collectionDAO.update(collectionEntity);
        removeArtifactsWithoutCollection(removedArtifactList);

        return collectionConverter.convertToDTO(collectionEntity);
    }

    /**
     * Check if removed artifacts are contained in other collections and delete artifacts without collections
     *
     * @param removedArtifactList
     */
    private void removeArtifactsWithoutCollection(List<ArtifactEntity> removedArtifactList) {
        List<CollectionEntity> collectionEntityList = collectionDAO.getList(new ArrayList<>());
        if (removedArtifactList == null || removedArtifactList.isEmpty()) {
            return;
        }
        collectionEntityList.forEach(collectionEntity -> {
            removedArtifactList.removeIf(artifactEntity -> {
                if (collectionEntity.getArtifactList() != null && !collectionEntity.getArtifactList().isEmpty()) {
                    return collectionEntity.getArtifactList().contains(artifactEntity);
                } else return false;
            });
        });

        removedArtifactList.forEach(artifactEntity -> {
            artifactDAO.delete(artifactEntity);
        });
    }

    /**
     * Get artifact which are removed from collection
     *
     * @param collectionEntity
     * @param oldCollectionEntity
     * @return
     */
    private List<ArtifactEntity> getRemovedArtifact(CollectionEntity collectionEntity, CollectionEntity oldCollectionEntity) {
        List<ArtifactEntity> removedArtifactList = new ArrayList<>();
        if (oldCollectionEntity.getArtifactList() == null || oldCollectionEntity.getArtifactList().isEmpty()) {
            return removedArtifactList;
        }
        if (collectionEntity.getArtifactList() == null || collectionEntity.getArtifactList().isEmpty()) {
            removedArtifactList.addAll(oldCollectionEntity.getArtifactList());
        } else {
            oldCollectionEntity.getArtifactList().forEach(artifactEntity -> {
                if (!collectionEntity.getArtifactList().contains(artifactEntity)) {
                    removedArtifactList.add(artifactEntity);
                }
            });
        }
        return removedArtifactList;
    }

    @Override
    public void delete(Long id, AuthUser user) {
        CollectionEntity collectionEntity = collectionDAO.get(id);
        if (collectionEntity == null) {
            throw new RecordNotFoundException(ErrorCode.COLLECTION_NOT_FOUND);
        }
        checkPermission(collectionEntity.getUser().getId(), user.getId());
        deleteCollection(collectionEntity);
        collectionDAO.delete(collectionEntity);
        removeArtifactsWithoutCollection(collectionEntity.getArtifactList());
    }

    private void deleteCollection(CollectionEntity collectionEntity) {
        for (CollectionEntity entity : collectionEntity.getCollectionList()) {
            deleteCollection(entity);
            collectionDAO.delete(entity);
            removeArtifactsWithoutCollection(entity.getArtifactList());
        }
    }

    public CollectionDTO getBySharedKey(String sharedKey) {
        CollectionEntity collectionEntity = collectionDAO.getBySharedKey(sharedKey);
        if (collectionEntity == null) {
            throw new RecordNotFoundException(ErrorCode.COLLECTION_NOT_FOUND);
        }
        return collectionConverter.convertToDTO(collectionEntity);
    }
}
