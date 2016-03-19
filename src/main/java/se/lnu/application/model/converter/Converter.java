package se.lnu.application.model.converter;

import se.lnu.application.model.dto.CommonDTO;
import se.lnu.application.model.entity.CommonEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Converter for converting entities to dto and vice versa
 *
 * @param <E> entity class
 * @param <D> dto class
 */
public interface Converter<E extends CommonEntity, D extends CommonDTO> {
    E convertToEntity(D dto);

    D convertToDTO(E entity);

    default List<D> convertToDTOList(List<E> list) {
        List<D> resultList = new ArrayList<>();
        if (list != null) {
            list.forEach((entity) -> {
                resultList.add(convertToDTO(entity));
            });
        }
        return resultList;
    }

    default List<E> convertToEntityList(List<D> list) {
        List<E> resultList = new ArrayList<>();
        if (list != null) {
            list.forEach((dto) -> {
                resultList.add(convertToEntity(dto));
            });
        }
        return resultList;
    }
}
