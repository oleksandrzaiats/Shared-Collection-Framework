package se.lnu.application.processor;

import se.lnu.application.model.dto.CommonDTO;
import se.lnu.application.model.exception.NoPermissionException;
import se.lnu.application.security.AuthUser;

import java.util.List;


/**
 * Interface for Processors.
 * <p>
 * Processors should contain all "business logic" for all data transfer objects in system.
 *
 * @param <D> Data transfer object class
 */
public interface Processor<D extends CommonDTO> {
    List<D> getAll(AuthUser user);

    D get(Long id);

    D create(D dto);

    D update(D dto, AuthUser user);

    void delete(Long id, AuthUser user);

    default void checkPermission(Long ownerId, Long actualId) {
        if(!ownerId.equals(actualId)) {
            throw new NoPermissionException();
        }
    }
}
