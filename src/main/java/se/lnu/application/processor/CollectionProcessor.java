package se.lnu.application.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.lnu.application.converter.CollectionConverter;
import se.lnu.application.dao.CollectionDAO;
import se.lnu.application.dto.CollectionDTO;
import se.lnu.application.entity.CollectionEntity;

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
    public List<CollectionDTO> getAll() {
        return collectionConverter.convertToDTOList(collectionDAO.getList());
    }

    @Override
    public CollectionDTO get(Long id) {
        return collectionConverter.convertToDTO(collectionDAO.get(id));
    }

    @Override
    public CollectionDTO create(CollectionDTO dto) {
        CollectionEntity collectionEntity = collectionConverter.convertToEntity(dto);
        collectionEntity = collectionDAO.create(collectionEntity);
        return collectionConverter.convertToDTO(collectionEntity);
    }

    @Override
    public CollectionDTO update(CollectionDTO dto) {
        // TODO add artifact removing logic
        CollectionEntity collectionEntity = collectionConverter.convertToEntity(dto);
        collectionEntity = collectionDAO.update(collectionEntity);
        return collectionConverter.convertToDTO(collectionEntity);
    }

    @Override
    public void delete(Long id) {
        // TODO add deleting logic
        CollectionEntity collectionEntity = collectionDAO.get(id);
        collectionDAO.delete(collectionEntity);
    }
}