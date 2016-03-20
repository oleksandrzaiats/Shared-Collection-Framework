package se.lnu.application.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.lnu.application.model.converter.CollectionConverter;
import se.lnu.application.model.dao.CollectionDAO;
import se.lnu.application.model.dto.CollectionDTO;
import se.lnu.application.model.entity.CollectionEntity;
import se.lnu.application.model.exception.ErrorCode;
import se.lnu.application.model.exception.RecordNotFoundException;
import se.lnu.application.security.AuthUser;
import se.lnu.application.security.UserRole;
import se.lnu.application.utils.Filtering;

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
    CollectionConverter collectionConverter;

    @Override
    public List<CollectionDTO> getAll(AuthUser user) {
        List<Filtering> filteringList = new ArrayList<>();
        if(!user.getRoles().contains(UserRole.ROLE_ADMIN)) {
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
        collectionEntity = collectionDAO.update(collectionEntity);
        return collectionConverter.convertToDTO(collectionEntity);
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
    }

    private void deleteCollection(CollectionEntity collectionEntity) {
        for (CollectionEntity entity : collectionEntity.getCollectionList()) {
            deleteCollection(entity);
            collectionDAO.delete(collectionEntity);
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
