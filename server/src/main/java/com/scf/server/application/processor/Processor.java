package com.scf.server.application.processor;

import com.scf.shared.dto.CommonDTO;
import com.scf.server.application.model.exception.NoPermissionException;
import com.scf.server.application.security.AuthUser;

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
