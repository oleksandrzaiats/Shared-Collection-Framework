package se.lnu.application.processor;

import se.lnu.application.model.dto.CommonDTO;
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

    D update(D dto);

    void delete(Long id);
}
